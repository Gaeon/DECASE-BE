package com.skala.decase.domain.project.controller.dto.response;

import com.skala.decase.domain.project.domain.Permission;

public record MemberInvitationResponse(
        String email,
        boolean accepted,
        Permission permission
) {
}
