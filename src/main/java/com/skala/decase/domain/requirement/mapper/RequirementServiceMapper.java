package com.skala.decase.domain.requirement.mapper;

import com.skala.decase.domain.document.domain.Document;
import com.skala.decase.domain.member.domain.Member;
import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.requirement.domain.Difficulty;
import com.skala.decase.domain.requirement.domain.Priority;
import com.skala.decase.domain.requirement.domain.Requirement;
import com.skala.decase.domain.requirement.domain.RequirementType;
import com.skala.decase.domain.requirement.service.dto.response.CreateRfpResponse;
import com.skala.decase.domain.source.domain.Source;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RequirementServiceMapper {

    public Requirement toREQEntity(CreateRfpResponse response, Member member, Project project, LocalDateTime now) {

        Requirement newReq = new Requirement();

        newReq.createInitialRequirement(
                response.id(),
                RequirementType.fromKorean(response.type()),
                response.category_large(),
                response.category_medium(),
                response.category_small(),
                response.description(),
                response.detailed_description(),
                Priority.fromKorean(response.importance()),
                Difficulty.fromKorean(response.difficulty()),
                now,
                project,
                member
        );
        return newReq;
    }

    public Source toSrcEntity(CreateRfpResponse response, Requirement requirement, Document document) {

        Source newReq = new Source();

        newReq.createSource(
                requirement,
                document,
                Integer.parseInt(response.source_pages()),  //int로 변환할까
                response.acceptance_criteria()
        );
        return newReq;
    }


}