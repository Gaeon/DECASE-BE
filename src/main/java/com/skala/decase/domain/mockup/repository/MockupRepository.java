package com.skala.decase.domain.mockup.repository;

import com.skala.decase.domain.mockup.domain.Mockup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MockupRepository extends JpaRepository<Mockup, Long> {
	List<Mockup> findAllByProject_ProjectIdAndRevisionCount(Long projectId, Integer revisionCount);
}
