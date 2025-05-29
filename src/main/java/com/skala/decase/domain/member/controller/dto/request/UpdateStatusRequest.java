package com.skala.decase.domain.member.controller.dto.request;

import com.skala.decase.domain.project.domain.ProjectStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(

        @NotNull(message = "프로젝트 상태는 필수입니다.")
        ProjectStatus status

) {
}