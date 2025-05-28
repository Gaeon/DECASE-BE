package com.skala.decase.domain.company.service;

import com.skala.decase.domain.company.controller.dto.response.CompanyResponse;
import com.skala.decase.domain.company.domain.Company;
import com.skala.decase.domain.company.mapper.CompanyMapper;
import com.skala.decase.domain.company.repository.CompanyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    /**
     * 회사 이름 키워드로 검색 키워드가 없으면 전체 목록을 반환합니다.
     *
     * @param keyword 키워드
     * @return 검색된 회사 리스트
     */
    public List<CompanyResponse> searchCompaniesByName(String keyword, Pageable pageable) {
        Page<Company> companyList = (keyword == null|| keyword.isBlank()) ? companyRepository.findAll(pageable)
                : companyRepository.findByKeyword(keyword, pageable);
        return companyList.stream().map(companyMapper::toResponse).toList();
    }
}
