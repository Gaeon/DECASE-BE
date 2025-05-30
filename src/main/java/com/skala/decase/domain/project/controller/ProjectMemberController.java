package com.skala.decase.domain.project.controller;

import com.skala.decase.domain.project.controller.dto.request.CreateMemberProjectRequest;
import com.skala.decase.domain.project.controller.dto.response.CreateMemberProjectResponse;
import com.skala.decase.domain.project.service.ProjectInvitationService;
import com.skala.decase.global.model.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Project Member API", description = "프로젝트 멤버 관리를 위한 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectMemberController {

    private final ProjectInvitationService projectInvitationService;

    /*
    프로젝트 멤버 추가, 삭제, 리스트 조회, 단일 조회, 권한 수정
     */

    @Operation(summary = "프로젝트 멤버 추가", description = "프로젝트 멤버 초대를 위한 API입니다.")
    @PostMapping("/{projectId}/members")
    public ResponseEntity<ApiResponse<CreateMemberProjectResponse>> createProjectMember(@PathVariable("projectId") long projectId, @RequestBody CreateMemberProjectRequest request) {
        CreateMemberProjectResponse response = projectInvitationService.createInvitation(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

}
