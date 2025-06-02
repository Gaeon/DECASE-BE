package com.skala.decase.domain.project.controller.dto.response;

public record JoinProjectResponse(
        boolean joined,
        String message,
        String email,
        String token
) {
}
