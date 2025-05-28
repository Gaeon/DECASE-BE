package com.skala.decase.domain.company.controller;

import com.skala.decase.domain.company.controller.dto.response.CompanyResponse;
import com.skala.decase.domain.company.domain.CompanyApiDocument;
import com.skala.decase.domain.company.service.CompanyService;
import com.skala.decase.global.model.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @PostMapping("/search")
    @CompanyApiDocument.SearchApiDoc
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> search(@RequestBody String keyword) {
        List<CompanyResponse> response = companyService.searchCompaniesByName(keyword);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

}
