package com.skala.decase.domain.project.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record ProjectDetailResponseDto (
	Long projectId,
	String name,
	Long scale,
	Date startDate,
	Date endDate,
	String description,
	String proposalPM
) {
}
