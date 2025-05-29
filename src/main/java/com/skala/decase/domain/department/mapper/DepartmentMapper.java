package com.skala.decase.domain.department.mapper;

import com.skala.decase.domain.department.controller.dto.response.DepartmentResponse;
import com.skala.decase.domain.department.domain.Department;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DepartmentMapper {

    public DepartmentResponse toResponse(Department department) {
        return new DepartmentResponse(
                department.getCompany().getCompanyId(),
                department.getDepartmentId(),
                department.getName()
        );
    }

}