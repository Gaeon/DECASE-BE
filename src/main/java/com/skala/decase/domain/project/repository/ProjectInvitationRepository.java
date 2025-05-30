package com.skala.decase.domain.project.repository;

import com.skala.decase.domain.project.domain.ProjectInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectInvitationRepository extends JpaRepository<ProjectInvitation, Long> {
    ProjectInvitation findByToken(String token);
}
