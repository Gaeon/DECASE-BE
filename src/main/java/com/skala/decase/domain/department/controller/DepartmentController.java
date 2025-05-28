package com.skala.decase.domain.department.controller;

import com.skala.decase.domain.company.domain.CompanyApiDocument;
import com.skala.decase.domain.department.controller.dto.request.FindDepartmentRequest;
import com.skala.decase.domain.department.controller.dto.response.DepartmentResponse;
import com.skala.decase.domain.department.service.DepartmentService;
import com.skala.decase.global.model.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @PostMapping("/search")
    @CompanyApiDocument.SearchApiDoc
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> search(@RequestBody FindDepartmentRequest request) {
        List<DepartmentResponse> response = departmentService.searchDepartmentsByName(request.companyId(),
                request.keyword());
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

}
