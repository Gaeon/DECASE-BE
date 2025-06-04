package com.skala.decase.domain.requirement.repository;

import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.requirement.domain.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {
	List<Requirement> findByProject_AndRevisionCountAndIsDeletedFalse(Project project, int revisionCount);
	List<Requirement> findByProject_AndIsDeletedFalse(Project project);

	@Query("SELECT MAX(r.revisionCount) FROM Requirement r WHERE r.project = :project")
	Integer getMaxRevisionCount(Project project);

	Optional<Requirement> findFirstByProjectAndRevisionCount(Project project, int revisionCount);

	/**
	 * 해당 프로젝트 내에서 가장 큰 revision count가 큰 수를 찾아옴
	 * @param project
	 * @return
	 */
	@Query("SELECT MAX(r.revisionCount) FROM Requirement r WHERE r.project = :project")
	Optional<Integer> findMaxRevisionCountByProject(@Param("project") Project project);

	/**
	 * 요구사항 정의서의 REQ id로 요구사항 정의서 내용 찾아오기
	 * @param id
	 * @return
	 */
	@Query("SELECT r FROM Requirement r WHERE r.reqIdCode = :reqIdCode and r.isDeleted=false")
	Optional<Requirement> findByReqIdCodeAndIsDeletedFalse(@Param("reqIdCode") String id);
}
