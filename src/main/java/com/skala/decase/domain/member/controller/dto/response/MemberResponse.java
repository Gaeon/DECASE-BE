package com.skala.decase.domain.member.controller.dto.response;

public record MemberResponse(
        long memberId,
        String id,
        String name,
        String email,
        String companyName,
        String departmentName
) {
}