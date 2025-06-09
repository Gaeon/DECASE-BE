package com.skala.decase.domain.mockup.controller;

import com.skala.decase.domain.document.controller.dto.DocumentResponse;
import com.skala.decase.domain.document.service.DocumentService;
import com.skala.decase.domain.mockup.service.MockupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Mockup API", description = "목업 관리를 위한 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects/{projectId}")
public class MockupController {

	private final MockupService mockupService;

	// 요구사항 리비전에 따른 목업 불러오기
	@Operation(summary = "목업 보기", description = "생성된 목업 뷰어를 지원합니다.")
	@GetMapping("/")
	public ResponseEntity<Map<Integer, List<String>>> getMockups(
			@PathVariable Long projectId) {
		return ResponseEntity.ok(mockupService.getMockupsGroupedByRevision(projectId));
	}

	@Operation(summary = "목업 다운로드", description = "생성된 목업 코드의 다운로드를 지원합니다. (.zip)")
	@PostMapping("/{revisionCount}")
	public ResponseEntity<Resource> downloadMockups(
			@PathVariable Long projectId,
			@PathVariable Integer revisionCount) {
		return mockupService.downloadMockups(projectId, revisionCount);
	}
}
