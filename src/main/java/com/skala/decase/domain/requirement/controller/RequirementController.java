package com.skala.decase.domain.requirement.controller;

import com.skala.decase.domain.requirement.controller.dto.RequirementDto;
import com.skala.decase.domain.requirement.domain.Requirement;
import com.skala.decase.domain.requirement.service.RepositoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Requirement API", description = "요구사항 관리를 위한 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/")
public class RequirementController {

	private final RepositoryService repositoryService;

	@GetMapping("/{projectId}/requirements/generated")
	public ResponseEntity<List<RequirementDto>> getGeneratedRequirements(
			@PathVariable Long projectId,
			@RequestParam(required = false) Integer revisionCount) {

		List<Requirement> requirements = (revisionCount == null)
				? repositoryService.getGeneratedRequirements(projectId)
				: repositoryService.getGeneratedRequirements(projectId, revisionCount);

		List<RequirementDto> dtoList = requirements.stream()
				.map(RequirementDto::fromEntity)
				.toList();

		return ResponseEntity.ok(dtoList);
	}
}
