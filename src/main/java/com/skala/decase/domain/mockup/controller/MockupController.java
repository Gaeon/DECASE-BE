package com.skala.decase.domain.mockup.controller;

import com.skala.decase.domain.mockup.domain.dto.MockupUploadResponse;
import com.skala.decase.domain.mockup.service.MockupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "Mockup API", description = "목업 관리를 위한 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects/mockups/{projectId}")
public class MockupController {

	private final MockupService mockupService;

	// 사이드바 - 요구사항 리비전에 따른 목업 불러오기
	@Operation(summary = "목업 보기", description = "생성된 목업 리비전-파일명 전송")
	@GetMapping("")
	public ResponseEntity< Map<Integer, List<String>> > getMockups(
			@PathVariable Long projectId) {
		return ResponseEntity.ok(mockupService.getMockupsGroupedByRevision(projectId));
	}

	@Operation(summary = "목업 코드", description = "생성된 목업 코드 전송")
	@GetMapping("/{revisionCount}/{fileName}")
	public ResponseEntity<Resource> getMockupCode(
			@PathVariable Long projectId,
			@PathVariable Integer revisionCount,
			@PathVariable String fileName) {
		return mockupService.getMockupCode(projectId, revisionCount, fileName);
	}

	@Operation(summary = "목업 다운로드", description = "생성된 목업 코드의 다운로드를 지원 (.zip)")
	@PostMapping("/{revisionCount}/download")
	public ResponseEntity<Resource> downloadMockups(
			@PathVariable Long projectId,
			@PathVariable Integer revisionCount) {
		return mockupService.downloadMockups(projectId, revisionCount);
	}

	@Operation(summary = "[테스트용] 목업 업로드", description = "목업 업로드")
	@PostMapping(path = "/{revisionCount}/upload", consumes = "multipart/form-data")
	public ResponseEntity<List<MockupUploadResponse>> uploadMockups(
			@PathVariable Long projectId,
			@PathVariable Integer revisionCount,
			@RequestPart("files") List<MultipartFile> files
	) throws Exception {
		return ResponseEntity.ok(mockupService.uploadMockups(projectId, revisionCount, files));
	}
}