package com.skala.decase.domain.project.controller.dto.response;

public record InvitationInfoResponse(
        long projectId,
        String projectName,
        String adminName
) {
}
