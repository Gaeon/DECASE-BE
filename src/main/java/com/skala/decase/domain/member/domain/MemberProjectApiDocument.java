package com.skala.decase.domain.member.domain;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Tag(name = "Member Project API", description = "사용자의 프로젝트 관리를 위한 api 입니다.")
public @interface MemberProjectApiDocument {

    @Operation(summary = "사용자의 프로젝트 목록", description = "사용자의 프로젝트 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 목록 조회 성공 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface GetProjectListApiDoc {
    }

    @Operation(summary = "프로젝트 상태 변경", description = "사용자의 프로젝트 진행 상태를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 상태 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ChangeStatusListApiDoc {
    }


}
