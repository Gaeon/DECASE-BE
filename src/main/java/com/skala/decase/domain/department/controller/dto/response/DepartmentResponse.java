package com.skala.decase.domain.department.controller.dto.response;

public record DepartmentResponse(
        long companyId,
        long departmentId,
        String name
) {
}