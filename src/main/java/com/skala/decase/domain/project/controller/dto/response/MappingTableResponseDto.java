package com.skala.decase.domain.project.controller.dto.response;

public record MappingTableResponseDto(
		String req_code,
		String name,
		String description,
		String docName,
		Integer pageNum,
		String relSentence
) {}