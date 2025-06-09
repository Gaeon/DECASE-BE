package com.skala.decase.domain.requirement.service;

import com.skala.decase.domain.document.domain.Document;
import com.skala.decase.domain.document.exception.DocumentException;
import com.skala.decase.domain.document.service.DocumentService;
import com.skala.decase.domain.member.domain.Member;
import com.skala.decase.domain.member.service.MemberService;
import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.project.service.ProjectService;
import com.skala.decase.domain.requirement.handler.DocumentSavedEvent;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RfpProcessingService {

    private final ApplicationEventPublisher eventPublisher;
    private final AsyncRfpProcessor asyncRfpProcessor;

    private final ProjectService projectService;
    private final MemberService memberService;
    private final DocumentService documentService;

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
        Document document = documentService.uploadRFP(project, member, file);

        try {
            // MultipartFile 내용을 미리 바이트 배열로 읽어서 저장 (임시 파일 삭제 문제 해결)
            byte[] fileContent = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            String contentType = file.getContentType();

            // 이벤트 - 트랜잭션 커밋 후 processInParallel 실행
            eventPublisher.publishEvent(
                    new DocumentSavedEvent(fileContent, originalFilename, contentType, projectId, memberId,
                            document.getDocId()));

        } catch (IOException e) {
            throw new DocumentException("파일 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 요구사항 정의서 생성, as-is 분석 에이전트 호출
     */
    public void processInParallel(MultipartFile file, Long projectId, Long memberId, String rfpDocId) {
        log.info("병렬 처리 시작 - 프로젝트: {}", projectId);

        CompletableFuture<Void> requirementsFuture = asyncRfpProcessor.processRequirements(file, projectId, memberId,
                rfpDocId);
        CompletableFuture<Void> asisFuture = asyncRfpProcessor.processASIS(projectId, memberId, file);

        // 비동기로 완료 처리 (get() 제거로 커넥션 누수 방지)
        CompletableFuture.allOf(requirementsFuture, asisFuture)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.error("병렬 처리 실패 - 프로젝트: {}", projectId, throwable);
                    } else {
                        log.info("병렬 처리 완료 - 프로젝트: {}", projectId);
                    }
                });

        // 즉시 반환 (블로킹하지 않음)
        log.info("병렬 처리 요청 완료 - 프로젝트: {} (백그라운드에서 계속 진행)", projectId);
    }
}
