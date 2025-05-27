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
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project")
@Getter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private long projectId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "domain_name", length = 100)
    private String domainName;

    private LocalDateTime createdDate;

    private int versionCount;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<MemberProject> membersProjects;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Document> documents;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Requirement> requirements;
}
