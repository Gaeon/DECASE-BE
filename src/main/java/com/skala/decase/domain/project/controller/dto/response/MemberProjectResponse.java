package com.skala.decase.domain.project.controller.dto.response;

import com.skala.decase.domain.project.domain.Permission;

public record MemberProjectResponse(
        long id,
        String memberId,
        String name,
        String company,
        String department,
        Permission permission
) {
}
