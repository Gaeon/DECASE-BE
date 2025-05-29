package com.skala.decase.domain.project.domain;

import com.skala.decase.domain.document.domain.Document;
import com.skala.decase.domain.requirement.domain.Requirement;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TM_PROJECTS")
@Getter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private long projectId;

    @Column(name = "name", length = 100)
    private String name;

    private long scale;  //프로젝트 규모

    private Date startDate;  //프로젝트 시작일

    private Date endDate;  //프로젝트 종료일

    @Column(name = "description", length = 1000)
    private String description;  //설명

    @Column(name = "proposal_pm", length = 100)
    private String proposalPM;  //제안 PM

    private int revisionCount;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<MemberProject> membersProjects;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Document> documents;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Requirement> requirements;
}
