package com.skala.decase.domain.document.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skala.decase.domain.document.controller.dto.DocumentDetailResponse;
import com.skala.decase.domain.document.controller.dto.DocumentResponse;
import com.skala.decase.domain.document.service.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Docs API", description = "문서 관리를 위한 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DocumentController {

    private final DocumentService documentService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/projects/{projectId}/documents/uploads", consumes = "multipart/form-data")
    public ResponseEntity<List<DocumentResponse>> uploadDocuments(
            @PathVariable Long projectId,
            @RequestParam Long memberId,
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("types") String typesJson
    ) throws Exception {
        List<Integer> types = objectMapper.readValue(typesJson, new TypeReference<>() {});
        return ResponseEntity.ok(documentService.uploadDocuments(projectId, memberId, files, types));
    }

    // @DeleteMapping("/{docId}")
    // public ResponseEntity<Void> deleteDocument(@PathVariable Long docId) {
    //     documentService.deleteDocument(docId);
    //     return ResponseEntity.noContent().build();
    // }

    @PostMapping("/documents/{docId}/downloads")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable String docId) throws Exception {
        return documentService.downloadDocument(docId);
    }

    @GetMapping("/documents/{docId}")
    public ResponseEntity<DocumentDetailResponse> getDocumentDetail(@PathVariable String docId) throws Exception {
        return documentService.getDocumentDetails(docId);
    }

    // 요구사항 리비전에 따른 문서 목록
    @GetMapping("/projects/{projectId}/document/uploads")
    public ResponseEntity<List<DocumentResponse>> getDocumentUploads(@PathVariable Long projectId) throws Exception {
        return documentService.getDocumentUploads(projectId);
    }

    @Operation(summary = "문서 미리보기", description = "사용자가 업로드한 문서의 미리보기를 지원합니다.")
    @GetMapping("/documents/{docId}/preview")
    public ResponseEntity<Resource> previewDocument(@PathVariable String docId){
        Resource resource=documentService.previewDocument(docId);

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
}