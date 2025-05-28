package com.skala.decase.domain.document.domain;

import com.skala.decase.domain.doc_type.domain.DocType;
import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.requirement.domain.RequirementDocument;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document")
@Data
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private long docId;

    @Column(name = "path", length = 1000)
    private String path;

    @Column(name = "name", length = 100)
    private String name;

    private LocalDateTime createdDate;

    private boolean isMemberUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_type_id")
    private DocType docType;

    @OneToMany(mappedBy = "document", fetch = FetchType.LAZY)
    private List<RequirementDocument> requirementDocuments;
}
