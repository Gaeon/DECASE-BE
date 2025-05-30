package com.skala.decase.domain.department.service;

import com.skala.decase.domain.department.controller.dto.response.DepartmentResponse;
import com.skala.decase.domain.department.domain.Department;
import com.skala.decase.domain.department.mapper.DepartmentMapper;
import com.skala.decase.domain.department.repository.DepartmentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    /**
     * 회사 이름 키워드로 검색 키워드가 없으면 전체 목록을 반환합니다.
     *
     * @param keyword 키워드
     * @return 검색된 부서 리스트
     */
    public List<DepartmentResponse> searchDepartmentsByName(long companyId, String keyword, Pageable pageable) {
        Page<Department> departmentList = (keyword == null|| keyword.isBlank()) ? departmentRepository.findByCompanyId(companyId, pageable)
                : departmentRepository.findByCompanyIdAndNameContaining(companyId, keyword, pageable);
        return departmentList.stream().map(departmentMapper::toResponse).toList();
    }
}
