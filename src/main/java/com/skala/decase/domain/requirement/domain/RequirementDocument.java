package com.skala.decase.domain.requirement.domain;

import com.skala.decase.domain.document.domain.Document;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TD_SOURCE")
@Getter
@NoArgsConstructor
public class RequirementDocument {

    @Id
    @Column(name = "source_id")
    private long sourceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "req_pk", insertable = false, updatable = false)
    private Requirement requirement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", insertable = false, updatable = false)
    private Document document;

    private int pageNum;  //페이지 번호

    private String relSentence;  //관련 문장

}
