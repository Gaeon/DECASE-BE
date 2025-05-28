package com.skala.decase.domain.doc_type.repository;

import com.skala.decase.domain.doc_type.domain.DocType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocTypeRepository extends JpaRepository<DocType, Integer> {
    Optional<DocType> findByName(String name);
}