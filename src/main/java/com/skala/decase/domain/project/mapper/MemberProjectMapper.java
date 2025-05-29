package com.skala.decase.domain.project.mapper;

import com.skala.decase.domain.member.domain.Member;
import com.skala.decase.domain.project.domain.MemberProject;
import com.skala.decase.domain.project.domain.Permission;
import com.skala.decase.domain.project.domain.Project;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MemberProjectMapper {

    public MemberProject toAdminEntity(Member member, Project project) {
        return new MemberProject(
                member,
                project,
                Permission.READ_AND_WRITE,
                true
        );
    }

}