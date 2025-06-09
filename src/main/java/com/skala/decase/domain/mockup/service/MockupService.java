package com.skala.decase.domain.mockup.service;

import com.skala.decase.domain.mockup.domain.Mockup;
import com.skala.decase.domain.mockup.exception.MockupException;
import com.skala.decase.domain.mockup.repository.MockupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class MockupService {

	private final MockupRepository mockupRepository;

	// 프로젝트 ID 기준 모든 목업 리비전별로 그룹화된 정보 반환
	public Map<Integer, List<String>> getMockupsGroupedByRevision(Long projectId) {
		List<Mockup> mockups = mockupRepository.findAllByProject_ProjectId(projectId);
		if (mockups.isEmpty()) {
			throw new MockupException("해당 프로젝트에 대한 목업이 없습니다.", HttpStatus.NOT_FOUND);
		}

		Map<Integer, List<String>> revisionMap = new HashMap<>();
		for (Mockup mockup : mockups) {
			revisionMap
					.computeIfAbsent(mockup.getRevisionCount(), k -> new ArrayList<>())
					.add(Paths.get(mockup.getPath()).getFileName().toString());
		}

		return revisionMap;
	}

	// 목업 불러오기
	private List<Resource> getMockupsByRevision(Long projectId, Integer revisionCount) {
		List<Mockup> mockups = mockupRepository.findAllByProject_ProjectIdAndRevisionCount(projectId, revisionCount);
		if (mockups.isEmpty()) {
			throw new MockupException("해당 조건에 맞는 목업이 없습니다.", HttpStatus.NOT_FOUND);
		}

		List<Resource> resources = new ArrayList<>();
		for (Mockup mockup : mockups) {
			Path filePath = Paths.get(mockup.getPath());
			if (!Files.exists(filePath)) continue;

			try {
				resources.add(new UrlResource(filePath.toUri()));
			} catch (MalformedURLException e) {
				throw new MockupException("파일 경로 오류: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return resources;
	}

	// 목업 다운로드
	public ResponseEntity<Resource> downloadMockups(Long projectId, Integer revisionCount) {
		List<Resource> mockupResources = getMockupsByRevision(projectId, revisionCount);
		if (mockupResources.isEmpty()) {
			throw new MockupException("압축할 목업 파일이 없습니다.", HttpStatus.NOT_FOUND);
		}

		try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			 ZipOutputStream zipOut = new ZipOutputStream(byteStream)) {

			for (Resource resource : mockupResources) {
				String fileName = Paths.get(resource.getFilename()).getFileName().toString();
				zipOut.putNextEntry(new ZipEntry(fileName));
				try (InputStream inputStream = resource.getInputStream()) {
					byte[] buffer = new byte[1024];
					int length;
					while ((length = inputStream.read(buffer)) >= 0) {
						zipOut.write(buffer, 0, length);
					}
					zipOut.closeEntry();
				}
			}

			zipOut.finish();
			return ResponseEntity.ok()
					.header("Content-Disposition", "attachment; filename=\"mockups.zip\"")
					.body(new ByteArrayResource(byteStream.toByteArray()));

		} catch (IOException e) {
			throw new MockupException("ZIP 파일 생성 중 오류 발생: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
