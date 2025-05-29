package com.skala.decase.domain.member.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.skala.decase.domain.project.domain.Permission;
import com.skala.decase.domain.project.domain.ProjectStatus;
import java.util.Date;

public record MemberProjectListResponse(
        Long projectId,
        String name,
        Long scale,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date startDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date endDate,
        String description,
        String proposalPM,
        Integer revisionCount,
        ProjectStatus status,
        String createdDate,
        String modifiedDate,
        // 사용자의 프로젝트 내 역할 정보
        Permission permission,
        Boolean isAdmin
) {
}