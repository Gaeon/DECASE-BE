package com.skala.decase.domain.document.repository;

import com.skala.decase.domain.document.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}