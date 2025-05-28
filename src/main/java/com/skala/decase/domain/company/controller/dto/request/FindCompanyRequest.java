package com.skala.decase.domain.company.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FindCompanyRequest(

        @NotBlank(message = "키워드를 입력해 주세요.")
        String keyword

) {
}