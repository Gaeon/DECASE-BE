package com.skala.decase.domain.project.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;

public record CreateProjectRequest(

        @NotBlank(message = "프로젝트명은 필수입니다.")
        @Size(max = 100, message = "프로젝트명은 100자 이하로 입력해주세요.")
        String name,

        @NotNull(message = "프로젝트 규모는 필수입니다.")
        Long scale,

        @NotNull(message = "시작일은 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date startDate,

        @NotNull(message = "종료일은 필수입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date endDate,

        @NotBlank(message = "설명은 필수입니다.")
        @Size(max = 1000, message = "설명은 1000자 이하로 입력해주세요.")
        String description,

        @Size(max = 100, message = "제안 PM은 100자 이하로 입력해주세요.")
        String proposalPM,

        @NotNull(message = "생성자 멤버 ID는 필수입니다.")
        Long creatorMemberId

) {
}