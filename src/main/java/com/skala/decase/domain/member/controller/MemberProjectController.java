package com.skala.decase.domain.member.controller;

import com.skala.decase.domain.member.controller.dto.response.MemberProjectListResponse;
import com.skala.decase.domain.member.service.MemberProjectService;
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
    @Operation(summary = "회원별 프로젝트 목록 조회", description = "특정 회원이 참여한 프로젝트 목록을 조회합니다.")
    @GetMapping("/{memberId}/projects")
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
