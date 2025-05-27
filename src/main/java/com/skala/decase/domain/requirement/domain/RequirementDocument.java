package com.skala.decase.domain.requirement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.skala.decase.domain.document.domain.Document;

@Entity
@Table(name = "requirement_document")
@Getter
@NoArgsConstructor
public class RequirementDocument {

    @Id
    @Column(name = "req_doc_id")
    private long reqDocId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "req_pk", insertable = false, updatable = false)
    private Requirement requirement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", insertable = false, updatable = false)
    private Document document;

}
