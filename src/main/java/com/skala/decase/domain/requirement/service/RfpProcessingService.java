package com.skala.decase.domain.requirement.service;

import com.skala.decase.domain.document.domain.Document;
import com.skala.decase.domain.document.service.DocumentService;
import com.skala.decase.domain.member.domain.Member;
import com.skala.decase.domain.member.service.MemberService;
import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.project.service.ProjectService;
import com.skala.decase.domain.requirement.exception.RequirementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RfpProcessingService {

    private final ProjectService projectService;
    private final MemberService memberService;
    private final DocumentService documentService;

    private final AsyncRfpProcessor asyncRfpProcessor;

    /**
     * 요구사항 정의서 최초 생성
     *
     * @param projectId
     * @param memberId
     * @param file      RFP 문서
     */
    @Transactional
    public void createRequirementsSpecification(Long projectId, Long memberId, MultipartFile file) {
        Project project = projectService.findByProjectId(projectId);
        Member member = memberService.findByMemberId(memberId);
        Document RFPdoc = documentService.uploadRFP(project, member, file);  //RFP 저장

        // Fast api로 요구사항 정의서, asis 보고서 받아와서 DB에 저장
        // 병렬 처리 시작
        processInParallel(file, project, member, RFPdoc);
    }

    /**
     * 요구사항 정의서 생성, as-is 분석 에이전트 호출
     */
    private void processInParallel(MultipartFile file, Project project, Member member, Document rfpDoc) {
        log.info("병렬 처리 시작 - 프로젝트: {}", project.getProjectId());

        CompletableFuture<Void> requirementsFuture = asyncRfpProcessor.processRequirements(file, project, member,
                rfpDoc);
        CompletableFuture<Void> asisFuture = asyncRfpProcessor.processASIS(project, member, file);

        // 두 작업 완료 대기
        try {
            CompletableFuture.allOf(requirementsFuture, asisFuture)
                    .get(300, TimeUnit.SECONDS);

            log.info("병렬 처리 완료 - 프로젝트: {}", project.getProjectId());

        } catch (Exception e) {
            log.error("병렬 처리 실패", e);
            throw new RequirementException("RFP 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
