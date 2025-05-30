package com.skala.decase.domain.requirement.service;

import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.project.exception.ProjectException;
import com.skala.decase.domain.project.repository.ProjectRepository;
import com.skala.decase.domain.requirement.controller.dto.RequirementDto;
import com.skala.decase.domain.requirement.domain.Requirement;
import com.skala.decase.domain.requirement.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepositoryService {

	private final RequirementRepository requirementRepository;
	private final ProjectRepository projectRepository;

	// 기본값 1로 받도록 하기 위함
	public List<Requirement> getGeneratedRequirements(Long projectId) {
		return getGeneratedRequirements(projectId, 1);
	}

	public List<Requirement> getGeneratedRequirements(Long projectId, int revisionCount) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectException("존재하지 않는 프로젝트 입니다.", HttpStatus.NOT_FOUND));
		return requirementRepository.findByProject_AndRevisionCountAndIsDeletedFalse(project, revisionCount);
	}
}
