package com.skala.decase.domain.member.controller;

import com.skala.decase.domain.member.controller.dto.request.LogInRequest;
import com.skala.decase.domain.member.controller.dto.request.SignUpRequest;
import com.skala.decase.domain.member.controller.dto.response.MemberResponse;
import com.skala.decase.domain.member.domain.AuthApiDocument;
import com.skala.decase.domain.member.service.AuthService;
import com.skala.decase.global.model.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth API", description = "로그인/회원가입 관리를 위한 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     *
     * @param request
     * @return
     */
    @PostMapping("/signup")
    @AuthApiDocument.SignUpApiDoc
    public ResponseEntity<ApiResponse<String>> signUp(@RequestBody SignUpRequest request) {
        authService.signUp(request);
        return ResponseEntity.ok().body(ApiResponse.created("회원가입 성공"));
    }

    /**
     * 로그인
     *
     * @param request
     * @return
     */
    @PostMapping("/login")
    @AuthApiDocument.LoginApiDoc
    public ResponseEntity<ApiResponse<MemberResponse>> login(@RequestBody LogInRequest request) {
        MemberResponse response = authService.login(request);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

}
