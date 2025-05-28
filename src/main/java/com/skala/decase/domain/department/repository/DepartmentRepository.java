package com.skala.decase.domain.department.repository;

import com.skala.decase.domain.department.domain.Department;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d WHERE d.company.companyId = :companyId AND d.name LIKE %:name%")
    List<Department> findByCompanyIdAndNameContaining(@Param("companyId") Long companyId, @Param("name") String name);
}
