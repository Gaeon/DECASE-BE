package com.skala.decase.domain.project.mapper;

import com.skala.decase.domain.project.controller.dto.response.MemberProjectResponse;
import com.skala.decase.domain.project.domain.MemberProject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProjectMemberMapper {

    public MemberProjectResponse toResponse(MemberProject memberProject) {
        return new MemberProjectResponse(
                memberProject.getMember().getMemberId(),
                memberProject.getMember().getId(),
                memberProject.getMember().getName(),
                memberProject.getMember().getCompany().getName(),
                memberProject.getMember().getCompany().getName(),
                memberProject.getPermission()
        );
    }
}
