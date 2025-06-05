package com.skala.decase.domain.requirement.mapper;


import com.skala.decase.domain.document.domain.Document;
import com.skala.decase.domain.member.domain.Member;
import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.requirement.controller.dto.response.RequirementWithSourceResponse;
import com.skala.decase.domain.requirement.controller.dto.response.SourceResponse;
import com.skala.decase.domain.requirement.domain.Difficulty;
import com.skala.decase.domain.requirement.domain.Priority;
import com.skala.decase.domain.requirement.domain.Requirement;
import com.skala.decase.domain.requirement.domain.RequirementType;
import com.skala.decase.domain.requirement.service.dto.response.CreateRfpResponse;
import com.skala.decase.domain.source.domain.Source;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
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

    public static RequirementWithSourceResponse toReqWithSrcResponse(Requirement requirement, List<String> modReason) {
        List<SourceResponse> sourceResponses = requirement.getSources().stream()
                .map(RequirementServiceMapper::toSourceResponse)
                .collect(Collectors.toList());

        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return new RequirementWithSourceResponse(
                requirement.getReqPk(),
                requirement.getReqIdCode(),
                requirement.getRevisionCount(),
                requirement.getType() != null ? requirement.getType().name() : null,
                requirement.getType() != null ? requirement.getType().name() : null, // status = type으로 가정
                requirement.getLevel1(),
                requirement.getLevel2(),
                requirement.getLevel3(),
                requirement.getPriority() != null ? requirement.getPriority().name() : null,
                requirement.getDifficulty() != null ? requirement.getDifficulty().name() : null,
                requirement.getName(),
                requirement.getDescription(),
                requirement.getCreatedDate() != null ? requirement.getCreatedDate().format(DATE_FORMATTER) : null,
                requirement.isDeleted(),
                requirement.getDeletedRevision(),
                modReason,
                sourceResponses
        );
    }

    private static SourceResponse toSourceResponse(Source source) {
        return new SourceResponse(
                source.getSourceId(),
                source.getDocument() != null ? source.getDocument().getDocId() : null,
                source.getPageNum(),
                source.getRelSentence()
        );
    }

}