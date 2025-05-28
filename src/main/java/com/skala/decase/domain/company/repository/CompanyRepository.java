package com.skala.decase.domain.company.repository;

import com.skala.decase.domain.company.domain.Company;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT c FROM Company c WHERE  c.name LIKE %:name%")
    List<Company> findByKeyword(@Param("name") String name);
}
