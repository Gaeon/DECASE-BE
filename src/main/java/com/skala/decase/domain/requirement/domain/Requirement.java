package com.skala.decase.domain.requirement.domain;

import com.skala.decase.domain.project.domain.Project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "requirement")
@Getter
@NoArgsConstructor
public class Requirement {

    @Id
    @Column(name = "req_pk")
    private long reqPk;

    @Column(name = "id", length = 100)
    private String id;

    @Enumerated(EnumType.STRING)
    private RequirementType type;

    @Column(name = "level_1", length = 100)
    private String level1;

    @Column(name = "level_2", length = 100)
    private String level2;

    @Column(name = "level_3", length = 100)
    private String level3;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 5000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    private int relPage;

    private String relSentence;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private int version;

    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private Project project;

    // 양방향 관계
    @OneToMany(mappedBy = "requirement", fetch = FetchType.LAZY)
    private List<RequirementDocument> requirementDocuments;
}
