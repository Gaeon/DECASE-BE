package com.skala.decase.domain.document.service;

import com.skala.decase.domain.document.controller.dto.DocumentDetailResponse;
import com.skala.decase.domain.document.domain.Document;
import com.skala.decase.domain.document.mapper.DocumentMapper;
import com.skala.decase.domain.document.repository.DocumentRepository;
import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.project.service.ProjectService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AsisService {

    private final ProjectService projectService;
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    /**
     * as-is 보고서 목록을 조회합니다.
     *
     * @param projectId
     * @return
     */
    public List<DocumentDetailResponse> getAsisDocumentList(Long projectId) {
        Project project = projectService.findByProjectId(projectId);

        // 해당 프로젝트의 모든 as-is 보고서 조회
        List<Document> documents = documentRepository.findByDocIdStartingWithASIS(project);

        return documents.stream()
                .map(documentMapper::toDetailResponse)
                .collect(Collectors.toList());

    }
}