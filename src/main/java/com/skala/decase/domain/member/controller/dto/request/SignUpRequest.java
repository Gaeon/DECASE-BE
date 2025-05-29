package com.skala.decase.domain.member.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignUpRequest(

        @NotBlank(message = "아이디를 입력해주세요.")
        String id,

        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\W).{8,16}$",
                message = "비밀번호는 8~16자 이내이며, 대문자, 소문자, 특수문자를 포함해야 합니다."
        )
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password,

        @NotBlank(message = "이름을 입력해주세요.")
        String name,

        @Schema(example = "decase@skala.ac.kr")
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
                message = "이메일 형식에 맞지 않습니다."
        )
        @NotBlank(message = "이메일을 입력해주세요.")
        String email,

        @Schema(description = "회사 식별자", example = "1")
        long companyId,

        @Schema(description = "부서 식별자", example = "1")
        long departmentId

) {}