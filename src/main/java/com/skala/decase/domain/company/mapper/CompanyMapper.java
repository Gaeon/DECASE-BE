package com.skala.decase.domain.company.mapper;

import com.skala.decase.domain.company.controller.dto.response.CompanyResponse;
import com.skala.decase.domain.company.domain.Company;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CompanyMapper {

    public CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getCompanyId(),
                company.getName()
        );
    }

}