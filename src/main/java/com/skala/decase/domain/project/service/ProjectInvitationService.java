package com.skala.decase.domain.project.service;

import com.skala.decase.domain.project.controller.dto.request.CreateMemberProjectRequest;
import com.skala.decase.domain.project.controller.dto.response.CreateMemberProjectResponse;
import com.skala.decase.domain.project.controller.dto.response.JoinProjectResponse;
import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.project.domain.ProjectInvitation;
import com.skala.decase.domain.project.exception.ProjectException;
import com.skala.decase.domain.project.mapper.SuccessMapper;
import com.skala.decase.domain.project.repository.ProjectInvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectInvitationService {

    private final SuccessMapper successMapper;
    private final ProjectInvitationRepository projectInvitationRepository;
    private final ProjectService projectService;
    private final MailService mailService;

    /*
        프로젝트 초대 정보 생성 및 메일 전송
     */

    public CreateMemberProjectResponse createInvitation(long projectId, CreateMemberProjectRequest request) {
        Project project = projectService.findByProjectId(projectId);

        ProjectInvitation projectInvitation = ProjectInvitation.builder()
                .email(request.email())
                .accepted(false)
                .permission(request.permission())
                .project(project)
                .build();
        mailService.sendMail(projectInvitation);
        projectInvitationRepository.save(projectInvitation);

        return successMapper.success();
    }

    public JoinProjectResponse accept(String token) {
        ProjectInvitation projectInvitation = projectInvitationRepository.findByToken(token);
        if (token == null) {
            throw new ProjectException("토큰이 유효하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        if (projectInvitation.isExpired()) {
            throw new ProjectException("초대 링크가 만료되었습니다.", HttpStatus.GONE);
        }

        return null;
    }
}
