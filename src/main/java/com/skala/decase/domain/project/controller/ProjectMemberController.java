package com.skala.decase.domain.project.controller;

import com.skala.decase.domain.project.controller.dto.request.ChangeStatusRequest;
import com.skala.decase.domain.project.controller.dto.request.CreateMemberProjectRequest;
import com.skala.decase.domain.project.controller.dto.request.DeleteMemberInvitationRequest;
import com.skala.decase.domain.project.controller.dto.response.*;
import com.skala.decase.domain.project.service.ProjectInvitationService;
import com.skala.decase.global.model.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Project Member API", description = "프로젝트 멤버 관리를 위한 API 입니다.")
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectMemberController {

    private final ProjectInvitationService projectInvitationService;

    /*
    프로젝트 멤버 추가, 삭제, 리스트 조회, 단일 조회, 권한 수정
     */

    @Operation(summary = "프로젝트 멤버 추가", description = "프로젝트 멤버 초대를 위한 API입니다.")
    @PostMapping("/{projectId}/members")
    public ResponseEntity<ApiResponse<List<CreateMemberProjectResponse>>> createInvitation(@PathVariable("projectId") long projectId, @RequestBody List<@Valid CreateMemberProjectRequest> requests) {
        List<CreateMemberProjectResponse> response = requests.stream()
                .map(request -> projectInvitationService.createInvitation(projectId, request))
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @Operation(summary = "프로젝트 참여 수락", description = "토큰을 기반으로 프로젝트 참여.")
    @PostMapping("/invitation")
    public ResponseEntity<ApiResponse<JoinProjectResponse>> acceptInvitation(@RequestParam("token") String token) {
        JoinProjectResponse response = projectInvitationService.accept(token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @Operation(summary = "프로젝트 멤버 리스트 조회", description = "프로젝트 참여 중인 멤버 목록 조회를 위한 API입니다.")
    @GetMapping("/{projectId}/members")
    public ResponseEntity<ApiResponse<List<MemberProjectResponse>>> findAllMember(@PathVariable("projectId") long projectId) {
        List<MemberProjectResponse> responses = projectInvitationService.findAllByProject(projectId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(responses));
    }

    @Operation(summary = "프로젝트 멤버 단일 조회", description = "프로젝트 참여 중인 멤버를 조회하기 위한 API입니다.")
    @GetMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<ApiResponse<MemberProjectResponse>> findMember(@PathVariable("projectId") long projectId, @PathVariable("memberId") String memberId) {
        MemberProjectResponse response = projectInvitationService.findMemberInProject(projectId, memberId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(response));
    }

    @Operation(summary = "프로젝트 멤버 권한 수정", description = "프로젝트 참여 중인 멤버의 권한을 수정하기 위한 API입니다.")
    @PatchMapping("/{projectId}/members/{memberId}/status")
    public ResponseEntity<ApiResponse<MemberProjectResponse>> updateMemberStatus(@PathVariable("projectId") long projectId, @PathVariable("memberId") String memberId, @RequestBody ChangeStatusRequest request) {
        MemberProjectResponse response = projectInvitationService.updateMemberStatus(projectId, memberId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.success(response));
    }

    @Operation(summary = "프로젝트 멤버 삭제", description = "프로젝트 멤버 삭제를 위한 API입니다.")
    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<ApiResponse<DeleteMemberResponse>> deleteMember(@PathVariable("projectId") long projectId, @PathVariable("memberId") String memberId) {
        DeleteMemberResponse response = projectInvitationService.deleteMember(projectId, memberId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(response));
    }

    @Operation(summary = "프로젝트 초대 현황 조회", description = "프로젝트 내 초대 현황을 조회하는 API입니다.")
    @GetMapping("/{projectId}/members/invitation")
    public ResponseEntity<ApiResponse<List<MemberInvitationResponse>>> findInviteMember(@PathVariable("projectId") long projectId) {
        List<MemberInvitationResponse> responses = projectInvitationService.findMemberInvitationByProject(projectId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(responses));
    }

    @Operation(summary = "프로젝트 초대 삭제", description = "프로젝트 초대를 삭제하는 API입니다.")
    @DeleteMapping("/{projectId}/members/invitation/cancel")
    public ResponseEntity<ApiResponse<DeleteMemberResponse>> deleteInvitation(@PathVariable("projectId") long projectId, @RequestBody DeleteMemberInvitationRequest request) {
        DeleteMemberResponse response = projectInvitationService.deleteMemberInvitation(projectId, request);
        return ResponseEntity.ok()
                .body(ApiResponse.success(response));
    }
}
