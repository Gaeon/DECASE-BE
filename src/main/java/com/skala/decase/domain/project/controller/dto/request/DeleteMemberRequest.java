package com.skala.decase.domain.project.controller.dto.request;

public record DeleteMemberRequest(
        long adminId,
        String memberId
) {
}
