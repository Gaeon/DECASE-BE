package com.skala.decase.domain.project.controller;

import com.skala.decase.domain.project.controller.dto.request.CreateProjectRequest;
import com.skala.decase.domain.project.controller.dto.response.ProjectResponse;
import com.skala.decase.domain.project.domain.ProjectApiDocument;
import com.skala.decase.domain.project.service.ProjectService;
import com.skala.decase.global.model.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("")
    @ProjectApiDocument.CreateApiDoc
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @Valid @RequestBody CreateProjectRequest request) {
        ProjectResponse response = projectService.createProject(request);
        return ResponseEntity.ok().body(ApiResponse.created(response));
    }

}
