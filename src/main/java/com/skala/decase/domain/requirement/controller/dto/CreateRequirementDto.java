package com.skala.decase.domain.requirement.controller.dto;

import com.skala.decase.domain.requirement.domain.Difficulty;
import com.skala.decase.domain.requirement.domain.Priority;
import com.skala.decase.domain.requirement.domain.Requirement;
import com.skala.decase.domain.requirement.domain.RequirementType;
import lombok.Data;
import org.apache.commons.lang3.builder.Diff;

@Data
public class CreateRequirementDto {
	private String reqIdCode;
	private Long memberId;
	private RequirementType type;
	private String level1;
	private String level2;
	private String level3;
	private Priority priority;
	private Difficulty difficulty;
	private String name;
	private String description;
	private String modReason;

//	public static CreateRequirementDto fromEntity() {
//
//	}
}
