package com.skala.decase.domain.document.service;

import com.skala.decase.domain.document.domain.Document;
import com.skala.decase.domain.document.controller.dto.DocumentResponse;
import com.skala.decase.domain.document.exception.DocumentException;
import com.skala.decase.domain.document.repository.DocumentRepository;
import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.project.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
	private final ProjectRepository projectRepository;

	// 로컬 파일 업로드 경로
    private final static String BASE_UPLOAD_PATH = "DECASE/upload";

    // 사용자 업로드
    public List<DocumentResponse> uploadDocuments(Long projectId, List<MultipartFile> files, List<Integer> types) throws IOException {
        if (files.size() != types.size()) {
			throw new DocumentException("파일 수와 타입 수가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        List<DocumentResponse> responses = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);

			Project project = projectRepository.findById(projectId)											// ⚠️ 추후 projectRepository 보고 수정 필요할 수 있음!
					.orElseThrow(() -> new DocumentException("유효하지 않은 프로젝트 ID: " + projectId, HttpStatus.NOT_FOUND));

            // 파일 저장
            String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
            Path uploadPath = Paths.get(BASE_UPLOAD_PATH);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Document 저장
            Document doc = new Document();
            doc.setName(file.getOriginalFilename());
            doc.setPath(filePath.toString());
            doc.setCreatedDate(LocalDateTime.now());
            doc.setMemberUpload(true);
            doc.setProject(project);

            documentRepository.save(doc);

        }

        return responses;
    }

    // 사용자 업로드 파일 삭제
    // 💡 삭제 왜 해야대..?
    // public void deleteDocument(Long docId) {
    //     documentRepository.deleteById(docId);
    // }

    // 사용자 업로드 파일 다운로드
    public ResponseEntity<byte[]> downloadDocument(@PathVariable String docId) throws IOException {
        Document doc = documentRepository.findById(docId)
				.orElseThrow(() -> new DocumentException("문서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        Path path = Paths.get(doc.getPath());
        byte[] content = Files.readAllBytes(path);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getName() + "\"")
                .body(content);
    }
}