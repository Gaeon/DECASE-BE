package com.skala.decase.domain.requirement.repository;

import com.skala.decase.domain.requirement.domain.Requirement;
import com.skala.decase.domain.requirement.domain.RequirementDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequirementDocumentRepository extends JpaRepository<RequirementDocument, Long> {
	RequirementDocument findByRequirement(Requirement requirement);
}
