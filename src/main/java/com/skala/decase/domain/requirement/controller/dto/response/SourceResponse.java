package com.skala.decase.domain.requirement.controller.dto.response;

public record SourceResponse(
        Long sourceId,
        String docId,
        int pageNum,
        String relSentence
) {
}