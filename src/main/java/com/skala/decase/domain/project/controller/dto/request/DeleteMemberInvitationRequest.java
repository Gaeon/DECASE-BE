package com.skala.decase.domain.project.controller.dto.request;

public record DeleteMemberInvitationRequest(
        long adminId,
        String email
) {
}
