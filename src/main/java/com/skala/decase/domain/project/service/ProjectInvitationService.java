package com.skala.decase.domain.project.service;

import com.skala.decase.domain.member.domain.Member;
import com.skala.decase.domain.member.repository.MemberProjectRepository;
import com.skala.decase.domain.member.service.MemberService;
import com.skala.decase.domain.project.controller.dto.request.ChangeStatusRequest;
import com.skala.decase.domain.project.controller.dto.request.CreateMemberProjectRequest;
import com.skala.decase.domain.project.controller.dto.request.DeleteMemberInvitationRequest;
import com.skala.decase.domain.project.controller.dto.request.DeleteMemberRequest;
import com.skala.decase.domain.project.controller.dto.response.*;
import com.skala.decase.domain.project.domain.MemberProject;
import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.project.domain.ProjectInvitation;
import com.skala.decase.domain.project.exception.ProjectException;
import com.skala.decase.domain.project.mapper.ProjectMemberMapper;
import com.skala.decase.domain.project.mapper.SuccessMapper;
import com.skala.decase.domain.project.repository.ProjectInvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectInvitationService {

    private final SuccessMapper successMapper;
    private final ProjectMemberMapper projectMemberMapper;
    private final ProjectInvitationRepository projectInvitationRepository;
    private final MemberProjectRepository memberProjectRepository;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final MailService mailService;

    /*
        프로젝트 초대 정보 생성 및 메일 전송
     */

    public CreateMemberProjectResponse createInvitation(long projectId, CreateMemberProjectRequest request) {
        Project project = projectService.findByProjectId(projectId);
        MemberProject adminMemberProject = memberProjectRepository.findByProjectIdAndMemberId(projectId, request.adminId());
        if (adminMemberProject == null) {
            throw new ProjectException("권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        if (adminMemberProject.getMember().getEmail().equals(request.email())) {
            throw new ProjectException("Admin에게 초대를 발송할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        ProjectInvitation preProjectInvitation = projectInvitationRepository.findFirstByProjectAndEmailOrderByExpiryDateDesc(project, request.email())
                .orElse(null);
        if (preProjectInvitation != null && !preProjectInvitation.isExpired()) {
            throw new ProjectException("이미 초대를 전송했습니다.", HttpStatus.BAD_REQUEST);
        }

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

        if (token == null || token.isBlank()) {
            throw new ProjectException("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ProjectInvitation projectInvitation = projectInvitationRepository.findByToken(token);

        if (projectInvitation == null) {
            throw new ProjectException("해당 토큰에 해당하는 초대가 없습니다.", HttpStatus.NOT_FOUND);
        }

        if (projectInvitation.isExpired()) {
            throw new ProjectException("초대 링크가 만료되었습니다.", HttpStatus.GONE);
        }

        Member newMember = memberService.findByMail(projectInvitation.getEmail());

        if (newMember == null) {
            return successMapper.isJoinSuccess(false, projectInvitation); //false일 경우 회원 가입
        }

        MemberProject memberProject = MemberProject.builder()
                .permission(projectInvitation.getPermission())
                .isAdmin(false)
                .member(newMember)
                .project(projectInvitation.getProject())
                .build();
        memberProjectRepository.save(memberProject);

        projectInvitation.setAcceptedTrue();
        projectInvitationRepository.save(projectInvitation);

        mailService.sendWelcomeMail(newMember, projectInvitation.getProject());

        return successMapper.isJoinSuccess(true, projectInvitation);
    }

    public List<MemberProjectResponse> findAllByProject(long projectId) {
        return memberProjectRepository.findAll()
                .stream()
                .filter(memberProject -> memberProject.getProject().getProjectId() == projectId)
                .map(projectMemberMapper::toResponse).toList();
    }

    public MemberProjectResponse findMemberInProject(long projectId, String memberId) {
        return memberProjectRepository.findByProjectIdAndId(projectId, memberId)
                .map(projectMemberMapper::toResponse)
                .orElseThrow(() -> new ProjectException("해당 프로젝트에 멤버가 존재하지 않습니다.", HttpStatus.NOT_FOUND));
    }

    public MemberProjectResponse updateMemberStatus(long projectId, String memberId, ChangeStatusRequest request) {
        MemberProject memberProject = memberProjectRepository.findByProjectIdAndId(projectId, memberId)
                .orElseThrow(() -> new ProjectException("해당 프로젝트에 멤버가 존재하지 않습니다.", HttpStatus.NOT_FOUND));
        MemberProject adminMemberProject = memberProjectRepository.findByProjectIdAndMemberId(projectId, request.adminId());

        if (adminMemberProject == null || !adminMemberProject.isAdmin()) {
            throw new ProjectException("수정 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        if (memberProject.isAdmin()) {
            throw new ProjectException("Admin의 권한을 변경할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        memberProject.setPermission(request.permission());
        memberProjectRepository.save(memberProject);
        return projectMemberMapper.toResponse(memberProject);
    }

    public DeleteMemberResponse deleteMember(long projectId, DeleteMemberRequest request) {
        MemberProject memberProject = memberProjectRepository.findByProjectIdAndId(projectId, request.memberId())
                .orElseThrow(() -> new ProjectException("해당 프로젝트에 멤버가 존재하지 않습니다.", HttpStatus.NOT_FOUND));
        MemberProject adminMemberProject = memberProjectRepository.findByProjectIdAndMemberId(projectId, request.adminId());
        if (adminMemberProject == null || !adminMemberProject.isAdmin()) {
            throw new ProjectException("권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        if (memberProject.isAdmin()) {
            throw new ProjectException("Admin을 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        memberProjectRepository.delete(memberProject);
        return projectMemberMapper.deleteSuccess();
    }

    public List<MemberInvitationResponse> findMemberInvitationByProject(long projectId) {
        Project project = projectService.findByProjectId(projectId);
        List<ProjectInvitation> projectInvitations = projectInvitationRepository.findAllByProject(project)
                .stream().filter(projectInvitation -> !projectInvitation.isExpired()).toList();

        return projectInvitations.stream().map(projectMemberMapper::toInvite).toList();
    }

    public DeleteMemberResponse deleteMemberInvitation(long projectId, DeleteMemberInvitationRequest request) {
        Project project = projectService.findByProjectId(projectId);
        MemberProject adminMemberProject = memberProjectRepository.findByProjectIdAndMemberId(projectId, request.adminId());
        ProjectInvitation projectInvitation = projectInvitationRepository.findFirstByProjectAndEmailOrderByExpiryDateDesc(project, request.email())
                .orElseThrow(() -> new ProjectException("존재하지 않는 초대입니다.", HttpStatus.BAD_REQUEST));

        if (adminMemberProject == null || !adminMemberProject.isAdmin()) {
            throw new ProjectException("권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }
        if (projectInvitation.isAccepted()) {
            throw new ProjectException("이미 수락된 초대입니다.", HttpStatus.BAD_REQUEST);
        }

        projectInvitationRepository.delete(projectInvitation);
        return projectMemberMapper.deleteInvitationSuccess();
    }
}
// 스케줄러로 유효기간 지나면 삭제 ?
