package com.skala.decase.domain.document.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skala.decase.domain.document.controller.dto.DocumentDetailResponse;
import com.skala.decase.domain.document.controller.dto.DocumentResponse;
import com.skala.decase.domain.document.service.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
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

    @Operation(summary = "사용자 문서 업로드", description = "사용자의 문서 업로드를 위한 API")
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

    @Operation(summary = "문서 다운로드", description = "문서 다운로드를 위한 API")
    @PostMapping("/documents/{docId}/downloads")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable String docId) throws Exception {
        return documentService.downloadDocument(docId);
    }

    @Operation(summary = "개별 문서 상세 정보 조회", description = "문서 상세 정보 조회를 위한 API")
    @GetMapping("/documents/{docId}")
    public ResponseEntity<DocumentDetailResponse> getDocumentDetail(@PathVariable String docId) throws Exception {
        return documentService.getDocumentDetails(docId);
    }

    // 요구사항 리비전에 따른 문서 목록
    @Operation(summary = "사용자 업로드 문서 목록 조회", description = "사용자 업로드 문서 목록 조회를 위한 API")
    @GetMapping("/projects/{projectId}/document/uploads")
    public ResponseEntity<List<DocumentResponse>> getDocumentUploads(@PathVariable Long projectId) throws Exception {
        return documentService.getDocumentUploads(projectId);
    }

    @Operation(summary = "저장된 문서 프리뷰", description = "문서 프리뷰를 위한 API")
    @GetMapping("/documents/{docId}/preview")
    public ResponseEntity<Resource> previewDocument(@PathVariable String docId) throws IOException {
        return documentService.previewDocument(docId);
    }
}