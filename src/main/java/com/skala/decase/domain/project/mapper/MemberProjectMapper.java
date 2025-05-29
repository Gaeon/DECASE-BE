package com.skala.decase.domain.project.mapper;

import com.skala.decase.domain.member.controller.dto.response.MemberProjectListResponse;
import com.skala.decase.domain.member.domain.Member;
import com.skala.decase.domain.project.domain.MemberProject;
import com.skala.decase.domain.project.domain.Permission;
import com.skala.decase.domain.project.domain.Project;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MemberProjectMapper {


    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MemberProjectListResponse toListResponse(MemberProject memberProject) {
        Project project = memberProject.getProject();

        return new MemberProjectListResponse(
                project.getProjectId(),
                project.getName(),
                project.getScale(),
                project.getStartDate(),
                project.getEndDate(),
                project.getDescription(),
                project.getProposalPM(),
                project.getRevisionCount(),
                project.getStatus(),
                project.getCreatedDate().format(FORMATTER),
                project.getModifiedDate().format(FORMATTER),
                memberProject.getPermission(),
                memberProject.isAdmin()
        );
    }

    public MemberProject toAdminEntity(Member member, Project project) {
        return new MemberProject(
                member,
                project,
                Permission.READ_AND_WRITE,
                true
        );
    }

}