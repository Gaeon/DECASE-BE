package com.skala.decase.domain.company.controller;

import com.skala.decase.domain.company.controller.dto.response.CompanyResponse;
import com.skala.decase.domain.company.domain.CompanyApiDocument;
import com.skala.decase.domain.company.service.CompanyService;
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

@Tag(name = "Company API", description = "회사 관리를 위한 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    /**
     * 회사 이름으로 검색
     */
    @GetMapping("/search")
    @CompanyApiDocument.SearchApiDoc
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> search(
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(size = 10, sort = "companyId", direction = Sort.Direction.DESC) Pageable pageable) {
        List<CompanyResponse> response = companyService.searchCompaniesByName(keyword, pageable);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

}
