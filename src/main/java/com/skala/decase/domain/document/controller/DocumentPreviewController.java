package com.skala.decase.domain.document.controller;

import com.skala.decase.domain.document.controller.dto.DocumentPreviewDto;
import com.skala.decase.domain.document.service.DocumentPreviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Docs Preview API", description = "문서 미리보기 관리를 위한 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DocumentPreviewController {

    private final DocumentPreviewService documentPreviewService;

    @Operation(summary = "문서 미리보기", description = "사용자가 업로드한 문서의 미리보기를 지원합니다.")
    @GetMapping("/documents/{docId}/preview")
    public ResponseEntity<Resource> previewDocument(@PathVariable String docId) {
        Resource resource = documentPreviewService.previewDocument(docId);

        //파일 이름 인코딩
        String filename = resource.getFilename();
        System.out.println(filename);
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename*=UTF-8''" + encodedFilename)
                .body(resource);
    }

    @Operation(summary = "문서 미리보기(docx, 엑셀)", description = "사용자가 업로드한 문서의 미리보기를 지원합니다.")
    @GetMapping("/documents/{docId}/info")
    public ResponseEntity<DocumentPreviewDto> getDocumentInfo(@PathVariable String docId) {
        DocumentPreviewDto previewDto = documentPreviewService.getDocumentPreview(docId);
        return ResponseEntity.ok(previewDto);
    }
}