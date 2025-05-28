package com.skala.decase.domain.member.mapper;

import com.skala.decase.domain.company.domain.Company;
import com.skala.decase.domain.department.domain.Department;
import com.skala.decase.domain.member.controller.dto.request.SignUpRequest;
import com.skala.decase.domain.member.controller.dto.response.MemberResponse;
import com.skala.decase.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MemberMapper {

    public Member toEntity(SignUpRequest request, Company company, Department department) {
        return new Member(
                request.id(),
                request.password(),
                request.name(),
                request.email(),
                company,
                department
        );
    }

    public MemberResponse toResponse(Member member) {
        return new MemberResponse(
                member.getMemberId(),
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getCompany().getName(),
                member.getDepartment().getName()
        );
    }

}