package com.skala.decase.domain.member.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LogInRequest(

        @NotBlank(message = "아이디를 입력해주세요.")
        String id,

        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\W).{8,16}$",
                message = "비밀번호는 8~16자 이내이며, 대문자, 소문자, 특수문자를 포함해야 합니다."
        )
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password

) {}