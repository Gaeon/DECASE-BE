package com.skala.decase.domain.project.controller;

import com.skala.decase.domain.project.controller.dto.request.CreateProjectRequest;
import com.skala.decase.domain.project.controller.dto.response.*;
import com.skala.decase.domain.project.domain.ProjectApiDocument;
import com.skala.decase.domain.project.service.ProjectService;
import com.skala.decase.global.model.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "Project API", description = "프로젝트 관리를 위한 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * 프로젝트 생성
     *
     * @param request
     * @return
     */
    @PostMapping
    @ProjectApiDocument.CreateApiDoc
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @Valid @RequestBody CreateProjectRequest request) {
        ProjectResponse response = projectService.createProject(request);
        return ResponseEntity.ok().body(ApiResponse.created(response));
    }

    // 프로젝트 수정
    @PutMapping("/{projectId}")
    public ResponseEntity<ApiResponse<EditProjectResponseDto>> updateProject(
            @PathVariable Long projectId,
            @RequestBody CreateProjectRequest request) {
        EditProjectResponseDto responseDto = projectService.editProject(projectId, request);
        return ResponseEntity.ok()
                .body(ApiResponse.success(responseDto));
    }

    // 프로젝트 삭제
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ApiResponse<DeleteProjectResponse>> deleteProject(
            @PathVariable Long projectId){
        DeleteProjectResponse response = projectService.deleteProject(projectId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(response));
    }

    // 단일 프로젝트 조회
    @GetMapping("/{projectId}")
    public ResponseEntity<ApiResponse<ProjectDetailResponseDto>> getProject(
            @PathVariable Long projectId) {
        ProjectDetailResponseDto responseDto = projectService.getProject(projectId);
        return ResponseEntity.ok()
                .body(ApiResponse.success(responseDto));
    }

    // 조견표 다운로드 임시 api
    @GetMapping("/projects/{projectId}/mapping-table/download")
    @Operation(summary = "(임시) 조견표 다운로드", description = "조견표 다운로드를 위한 임시 API입니다.")
    public ResponseEntity<byte[]> downloadMappingTable(
            @PathVariable Long projectId) throws IOException {
        byte[] excelFile = projectService.exportMappingTableToExcel(projectId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mapping_table.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }
}
