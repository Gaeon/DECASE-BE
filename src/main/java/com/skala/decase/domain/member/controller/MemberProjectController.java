package com.skala.decase.domain.member.controller;

import com.skala.decase.domain.member.controller.dto.request.UpdateStatusRequest;
import com.skala.decase.domain.member.controller.dto.response.MemberProjectListResponse;
import com.skala.decase.domain.member.domain.MemberProjectApiDocument;
import com.skala.decase.domain.member.service.MemberProjectService;
import com.skala.decase.domain.project.controller.dto.response.ProjectResponse;
import com.skala.decase.domain.project.domain.ProjectStatus;
import com.skala.decase.global.model.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member Project API", description = "회원의 프로젝트 관리를 위한 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberProjectController {

    private final MemberProjectService memberProjectService;

    /**
     * memberId의 프로젝트 목록 조회
     */
    @GetMapping("/{memberId}/projects")
    @MemberProjectApiDocument.GetProjectListApiDoc
    public ResponseEntity<ApiResponse<List<MemberProjectListResponse>>> getMemberProjects(
            @PathVariable Long memberId,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(required = false) String proposalPM,
            Pageable pageable) {
        List<MemberProjectListResponse> response = memberProjectService.getProjectsByMember(memberId, projectName,
                status, proposalPM, pageable);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }
}
