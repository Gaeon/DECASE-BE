package com.skala.decase.domain.requirement.service;

import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.project.exception.ProjectException;
import com.skala.decase.domain.project.repository.ProjectRepository;
import com.skala.decase.domain.requirement.domain.Requirement;
import com.skala.decase.domain.requirement.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RequirementService {

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

	public Map<String, List<String>> getRequirementCategory(Long projectId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectException("존재하지 않는 프로젝트 입니다.", HttpStatus.NOT_FOUND));

		List<Requirement> requirements = requirementRepository.findByProject_AndIsDeletedFalse(project);

		Set<String> level1Set = new HashSet<>();
		Set<String> level2Set = new HashSet<>();
		Set<String> level3Set = new HashSet<>();

		for (Requirement req : requirements) {
			if (req.getLevel1() != null) level1Set.add(req.getLevel1());
			if (req.getLevel2() != null) level2Set.add(req.getLevel2());
			if (req.getLevel3() != null) level3Set.add(req.getLevel3());
		}

		Map<String, List<String>> categoryMap = new HashMap<>();
		categoryMap.put("대분류", new ArrayList<>(level1Set));
		categoryMap.put("중분류", new ArrayList<>(level2Set));
		categoryMap.put("소분류", new ArrayList<>(level3Set));

		return categoryMap;
	}
}
