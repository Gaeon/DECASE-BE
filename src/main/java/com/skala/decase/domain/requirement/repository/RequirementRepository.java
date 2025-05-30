package com.skala.decase.domain.requirement.repository;

import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.requirement.domain.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {
	List<Requirement> findByProject_AndRevisionCountAndIsDeletedFalse(Project project, int revisionCount);
}
