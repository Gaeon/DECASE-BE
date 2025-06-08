package com.skala.decase.domain.source.service;

import com.skala.decase.domain.requirement.domain.Requirement;
import com.skala.decase.domain.source.domain.Source;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends JpaRepository<Source, Long> {

    @Query(value = """
            SELECT s.*
            FROM td_source s
            INNER JOIN td_requirements r ON s.req_pk = r.req_pk
            WHERE s.req_pk IN :reqPks
              AND r.revision_count <= :revisionCount
            """, nativeQuery = true)
    List<Source> findSourcesByRequirementsAndRevision(@Param("reqPks") List<Long> reqPks,
                                                      @Param("revisionCount") int revisionCount);

    @Query(value = """
            SELECT s.*
            FROM td_source s
            WHERE s.req_pk IN :reqPks
            """, nativeQuery = true)
    List<Source> findByRequirementReqPks(@Param("reqPks") List<Long> reqPks);


    List<Source> findAllByRequirement(Requirement requirement);
}
