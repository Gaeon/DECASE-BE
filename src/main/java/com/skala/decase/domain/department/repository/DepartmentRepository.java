package com.skala.decase.domain.department.repository;

import com.skala.decase.domain.department.domain.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d WHERE d.company.companyId = :companyId AND d.name LIKE %:name%")
    Page<Department> findByCompanyIdAndNameContaining(@Param("companyId") Long companyId, @Param("name") String name,
                                                      Pageable pageable);

    @Query("SELECT d FROM Department d WHERE d.company.companyId = :companyId")
    Page<Department> findByCompanyId(long companyId, Pageable pageable);
}
