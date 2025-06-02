package com.skala.decase.domain.requirement.controller;

import com.skala.decase.domain.requirement.service.RfpProcessingService;
import com.skala.decase.global.model.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "RFP API", description = "요구사항 정의서 관리를 위한 api 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/")
public class RfpProcessingController {

    private final RfpProcessingService rfpProcessingService;

    //TODO : 사용자 업로드 파일을 업로드 controller에서 처리를 해줄지 업로드 서비스에서 저장을 해줄지 고민
    //지금은 일단 문서 id로 가져오는걸로 함
    /**
     * 요구사항 정의서 생성
     *
     * @param projectId 프로젝트 id
     * @param memberId  멤버 id
     * @param docId     문서 id
     * @param file      사용자 업로드 파일
     * @return
     */
    @PostMapping(path="/{projectId}/requirement-documents",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> processRfpFile(@PathVariable Long projectId,
                                                            @RequestParam("memberId") Long memberId,
                                                            @RequestParam("docId") String docId,
                                                            @RequestPart("file") MultipartFile file) {
        // post: /api/v1/process-rfp-file에서 생성된 요구사항 정의서 리스트를 받아옴.
        rfpProcessingService.createRFP(projectId, memberId, docId, file);
        return ResponseEntity.ok().build();
    }

}
