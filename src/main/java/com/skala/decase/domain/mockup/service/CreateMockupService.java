package com.skala.decase.domain.mockup.service;


import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateMockupService {

    private final ProjectService projectService;
    private final MockupAsyncService mockupAsyncService;


    /**
     * 목업 생성 - fast api 서버에서 생성한 html/css 파일들을 받아옵니다.
     */
    public void createMockUpAsync(Long projectId, Integer revisionCount, String outputFolderName) {
        Project project = projectService.findByProjectId(projectId);

        // 비동기로 실제 목업 생성 작업 시작
        mockupAsyncService.processMockupGenerationAsync(projectId, revisionCount,
                outputFolderName);
    }


}
