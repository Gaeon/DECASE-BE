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
	@Column(name = "moc_id", length = 15, nullable = false)
	private String mocId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@Column(name = "revision_count", nullable = false)
	private Integer revisionCount;

	@Column(name = "path", length = 255, nullable = false)
	private String path;

	@Column(name = "created_date", nullable = false)
	private LocalDateTime createdDate;
}
