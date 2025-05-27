package com.skala.decase.domain.doc_type.domain;

import com.skala.decase.domain.document.domain.Document;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "doc_type")
@Getter
@NoArgsConstructor
public class DocType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_type_id")
    private int docTypeId;

    @Column(name = "name", length = 10)
    private String name;

    @OneToMany(mappedBy = "docType", fetch = FetchType.LAZY)
    private List<Document> documents;
}
