package com.skala.decase.domain.requirement.controller.dto.response;

import java.util.List;

public record RequirementWithSourceResponse(
        long reqPk,
        String reqIdCode,
        int revisionCount,
        String type,
        String status,
        String level1,
        String level2,
        String level3,
        String priority,
        String difficulty,
        String name,
        String description,
        String createdDate,
        boolean isDeleted,
        int deletedRevision,
        List<String> modReason,
        List<SourceResponse> sources
) {
}