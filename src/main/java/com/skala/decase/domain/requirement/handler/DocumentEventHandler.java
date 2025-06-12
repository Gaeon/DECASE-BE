package com.skala.decase.domain.requirement.handler;

import com.skala.decase.domain.requirement.domain.InMemoryMultipartFile;
import com.skala.decase.domain.requirement.service.SrsProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentEventHandler {

    private final SrsProcessingService srsProcessingService;

    @Async("taskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDocumentSaved(DocumentSavedEvent event) {
        log.info("Document 저장 완료 이벤트 수신 - DocumentId: {}", event.getDocumentId());

        try {
            // 바이트 배열을 InMemoryMultipartFile로 변환
            MultipartFile file = new InMemoryMultipartFile(
                    event.getFileContent(),
                    "file",
                    event.getOriginalFilename(),
                    event.getContentType()
            );

            // 트랜잭션 완전 커밋 후 실행
            srsProcessingService.processInParallel(
                    file,
                    event.getProjectId(),
                    event.getMemberId(),
                    event.getDocumentId()
            );
        } catch (Exception e) {
            log.error("Document 저장 후 비동기 처리 실패", e);
        }
    }
}
