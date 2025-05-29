package com.skala.decase.domain.department.domain;

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

@Tag(name = "department API", description = "부서 관리를 위한 api 입니다.")
public @interface DepartmentApiDocument {

    @Operation(summary = "부서 검색", description = "키워드가 없으면 companyId에 해당하는 모든 부서 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json",
                    examples = {@ExampleObject(name = "회사를 찾을 수 없습니다.",
                            value = "{\"code\": 404, \"message\": \"회사를 찾을 수 없습니다.\"}"),
                            @ExampleObject(name = "부서를 찾을 수 없습니다.",
                                    value = "{\"code\": 404, \"message\": \"부서를 찾을 수 없습니다.\"}"),
                    }
            )),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface SearchApiDoc {
    }

}
