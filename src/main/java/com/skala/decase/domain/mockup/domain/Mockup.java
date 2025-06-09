package com.skala.decase.domain.mockup.domain;

import com.skala.decase.domain.project.domain.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "TM_MOCKUP")
@NoArgsConstructor
public class Mockup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "moc_id", length = 255, nullable = false)
	private Long mocId;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@Column(name = "revision_count", nullable = false)
	private Integer revisionCount;

	@Column(name = "path", length = 255, nullable = false)
	private String path;

	@Column(name = "created_date", nullable = false)
	private LocalDateTime createdDate;

	@Builder
	public Mockup(String name, Project project, Integer revisionCount, String path) {
		this.name = name;
		this.project = project;
		this.revisionCount = revisionCount;
		this.path = path;
		this.createdDate = LocalDateTime.now();
	}
}
