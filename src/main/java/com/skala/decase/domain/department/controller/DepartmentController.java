package com.skala.decase.domain.department.controller;

import com.skala.decase.domain.company.domain.CompanyApiDocument;
import com.skala.decase.domain.department.controller.dto.response.DepartmentResponse;
import com.skala.decase.domain.department.service.DepartmentService;
import com.skala.decase.global.model.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Department API", description = "부서 관리를 위한 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * 회사 이름으로 검색
     */
    @GetMapping("/search")
    @CompanyApiDocument.SearchApiDoc
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> search(@RequestParam("companyId") long companyId,
                                                                        @RequestParam(value = "keyword", required = false) String keyword,
                                                                        @PageableDefault(size = 10, sort = "departmentId", direction = Sort.Direction.DESC) Pageable pageable) {
        List<DepartmentResponse> response = departmentService.searchDepartmentsByName(companyId, keyword, pageable);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

}
