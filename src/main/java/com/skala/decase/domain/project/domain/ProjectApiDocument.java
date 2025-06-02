package com.skala.decase.domain.project.domain;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Tag(name = "Project API", description = "프로젝트 관리를 위한 api 입니다.")
public @interface ProjectApiDocument {

    @Operation(summary = "프로젝트 생성", description = "프로젝트 생성입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "프로젝트 생성 성공 성공"),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json",
                    examples = {@ExampleObject(name = "잘못된 요청 데이터",
                            value = "{\"code\": 400, \"message\": \"잘못된 요청 데이터\"}"),
                            @ExampleObject(name = "프로젝트 규모는 0 이상이어야 합니다.",
                                    value = "{\"code\": 400, \"message\": \"프로젝트 규모는 0 이상이어야 합니다.\"}"),
                            @ExampleObject(name = "종료일은 시작일 이후여야 합니다.",
                                    value = "{\"code\": 400, \"message\": \"종료일은 시작일 이후여야 합니다.\"}"),
                    }
            )),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface CreateApiDoc {
    }


}
