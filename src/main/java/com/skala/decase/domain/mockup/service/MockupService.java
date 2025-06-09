package com.skala.decase.domain.mockup.service;

import com.skala.decase.domain.mockup.domain.Mockup;
import com.skala.decase.domain.mockup.exception.MockupException;
import com.skala.decase.domain.mockup.repository.MockupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MockupService {

	private final MockupRepository mockupRepository;

	public List<Resource> getMockups(Long projectId, Integer revisionCount) {
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
}
