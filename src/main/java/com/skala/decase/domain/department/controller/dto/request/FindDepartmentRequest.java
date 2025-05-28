package com.skala.decase.domain.department.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FindDepartmentRequest(

        @NotBlank(message = "회사 식별자를 입력해주세요.")
        long companyId,

        @NotBlank(message = "키워드를 입력해 주세요.")
        String keyword

) {
}