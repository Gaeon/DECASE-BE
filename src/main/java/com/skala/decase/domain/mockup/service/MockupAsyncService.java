package com.skala.decase.domain.mockup.service;


import com.skala.decase.domain.mockup.controller.dto.response.CreateMockUpRequest;
import com.skala.decase.domain.mockup.domain.Mockup;
import com.skala.decase.domain.mockup.exception.MockupException;
import com.skala.decase.domain.mockup.mapper.MockupMapper;
import com.skala.decase.domain.mockup.repository.MockupRepository;
import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.project.service.ProjectService;
import com.skala.decase.domain.requirement.controller.dto.response.RequirementWithSourceResponse;
import com.skala.decase.domain.requirement.service.RequirementService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class MockupAsyncService {

    private final WebClient webClient;
    private final MockupRepository mockupRepository;
    private final ProjectService projectService;
    private final RequirementService requirementService;
    private final MockupMapper mockupMapper;

    // 로컬 파일 업로드 경로
    private static final String BASE_UPLOAD_PATH = "DECASE/mockups";


    /**
     * 실제 목업 생성 작업 (비동기 실행)
     */
    @Async("mockupTaskExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CompletableFuture<Void> processMockupGenerationAsync(Long projectId, Integer revisionCount,
                                                                String outputFolderName) {
        try {
            log.info("비동기 목업 생성 작업 시작: projectId={}", projectId);
            Project project = projectService.findByProjectId(projectId);
            List<RequirementWithSourceResponse> requirementList = requirementService.getGeneratedRequirements(projectId,
                    revisionCount);

            List<CreateMockUpRequest> srsRequests = mockupMapper.toCreateMockUpRequestList(requirementList);
            // 4. FastAPI 서버에 요청하고 ZIP 파일 받기
            Resource zipResource = callFastApiMockupGeneration(srsRequests, outputFolderName);
            // 5. ZIP 파일 압축 해제 및 저장
            extractAndSaveMockupFiles(zipResource, project, revisionCount);
            log.info("목업 생성 작업 완료: projectId={}", projectId);

            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("목업 생성 작업 실패: projectId={}, revisionCount={}", projectId, revisionCount, e);
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * FastAPI 서버에 목업 생성 요청
     */
    private Resource callFastApiMockupGeneration(List<CreateMockUpRequest> srsRequests, String outputFolderName) {
        try {
            log.info("FastAPI 서버에 목업 생성 요청 시작. 요구사항 수: {}", srsRequests.size());

            return webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/mockup/generate-mockup")
                            .queryParamIfPresent("output_folder_name", Optional.ofNullable(outputFolderName))
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(srsRequests))
                    .retrieve()
                    .bodyToMono(Resource.class)
                    .timeout(Duration.ofMinutes(10))
                    .doOnSuccess(resource -> log.info("FastAPI 서버에서 ZIP 파일 수신 완료"))
                    .doOnError(error -> log.error("FastAPI 서버 호출 실패", error))
                    .block();

        } catch (Exception e) {
            log.error("FastAPI 서버 호출 실패", e);
            throw new MockupException("목업 생성 API 호출 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ZIP 파일 압축 해제 및 저장
     */
    @Transactional
    public void extractAndSaveMockupFiles(Resource zipResource, Project project, Integer revisionCount)
            throws IOException {

        // 프로젝트별 디렉토리 생성
        Path projectDir = Paths.get(BASE_UPLOAD_PATH, "project_" + project.getProjectId(), "revision_" + revisionCount);
        Files.createDirectories(projectDir);

        // ZIP 파일 압축 해제
        try (ZipInputStream zipInputStream = new ZipInputStream(zipResource.getInputStream())) {
            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    // 디렉토리 생성
                    Path dirPath = projectDir.resolve(entry.getName());
                    Files.createDirectories(dirPath);
                    continue;
                }

                // 파일 저장
                String filename = entry.getName();
                Path filePath = projectDir.resolve(filename);

                // 상위 디렉토리가 없으면 생성
                Files.createDirectories(filePath.getParent());

                // 파일 복사
                Files.copy(zipInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

                // Mockup 엔티티 생성 및 저장
                Mockup mockup = Mockup.builder()
                        .name(filename)
                        .project(project)
                        .revisionCount(revisionCount)
                        .path(filePath.toString())
                        .build();

                mockupRepository.save(mockup);

                log.info("목업 파일 저장 완료: {}", filename);
            }
        }
    }

    /**
     * 특정 프로젝트의 목업 파일들 조회
     */
//    public List<Mockup> getMockupsByProject(Long projectId, Integer revisionCount) {
//        return mockupRepository.findByProjectIdAndRevisionCount(projectId, revisionCount);
//    }
    private String getFileType(String filename) {
        if (filename == null) {
            return "UNKNOWN";
        }

        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

        return switch (extension) {
            case "html", "htm" -> "HTML";
            case "css" -> "CSS";
            case "js" -> "JAVASCRIPT";
            case "png", "jpg", "jpeg", "gif", "svg" -> "IMAGE";
            case "json" -> "JSON";
            default -> "OTHER";
        };
    }


}
