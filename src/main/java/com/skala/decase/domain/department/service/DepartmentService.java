package com.skala.decase.domain.department.service;

import com.skala.decase.domain.department.controller.dto.response.DepartmentResponse;
import com.skala.decase.domain.department.domain.Department;
import com.skala.decase.domain.department.mapper.DepartmentMapper;
import com.skala.decase.domain.department.repository.DepartmentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;

    /**
     * 회사 이름 키워드로 검색
     *
     * @param keyword 키워드
     * @return 검색된 부서 리스트
     */
    public List<DepartmentResponse> searchDepartmentsByName(long companyId, String keyword) {

        List<Department> departmentList = departmentRepository.findByCompanyIdAndNameContaining(companyId, keyword);
        return departmentList.stream().map(departmentMapper::toResponse).toList();
    }
}
