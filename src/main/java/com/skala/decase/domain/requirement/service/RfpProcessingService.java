package com.skala.decase.domain.requirement.service;

import com.skala.decase.domain.document.domain.Document;
import com.skala.decase.domain.document.exception.DocumentException;
import com.skala.decase.domain.document.repository.DocumentRepository;
import com.skala.decase.domain.member.domain.Member;
import com.skala.decase.domain.member.service.MemberService;
import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.project.service.ProjectService;
import com.skala.decase.domain.requirement.domain.Requirement;
import com.skala.decase.domain.requirement.mapper.RequirementServiceMapper;
import com.skala.decase.domain.requirement.repository.RequirementRepository;
import com.skala.decase.domain.requirement.service.dto.response.CreateRfpResponse;
import com.skala.decase.domain.source.domain.Source;
import com.skala.decase.domain.source.service.SourceRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RfpProcessingService {

    private final WebClient webClient;
    private final RequirementServiceMapper requirementServiceMapper;

    private final RequirementRepository requirementRepository;
    private final DocumentRepository documentRepository;
    private final SourceRepository sourceRepository;

    private final ProjectService projectService;
    private final MemberService memberService;

    private List<CreateRfpResponse> fetchRequirements(Project project, Member member, MultipartFile file,
                                                      Document document) {
        // 1. 파일을 FastAPI로 전송하고 결과 받아오기
//        String boundary = UUID.randomUUID().toString();
//        Flux<DataBuffer> fileContent = DataBufferUtils.readInputStream(
//                file::getInputStream,
//                new DefaultDataBufferFactory(),
//                4096
//        );

//        List<CreateRfpResponse> responseList = webClient.post()
//                .uri("/api/v1/process-rfp-file")
//                .contentType(MediaType.MULTIPART_FORM_DATA)
//                .body(BodyInserters.fromMultipartData("file",
//                        new MultipartBodyBuilder().part("file", file.getResource()).build().toSingleValueMap()
//                                .get("file")))
//                .retrieve()
//                .bodyToFlux(CreateRfpRequest.class)
//                .collectList()
//                .block();

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource());

        return webClient.post()
                .uri("/api/v1/process-rfp-file")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToFlux(CreateRfpResponse.class)
                .collectList()
                .block();

    }

    /**
     * RFP 생성
     *
     * @param projectId
     * @param memberId
     * @param docId
     * @param file
     */
    @Transactional
    public void createRFP(Long projectId, Long memberId, String docId, MultipartFile file) {
        Project project = projectService.findByProjectId(projectId);
        Member member = memberService.findByMemberId(memberId);
        //일단 RFP 기준
        Document document = documentRepository.findByDocId(docId)
                .orElseThrow(() -> new DocumentException("문서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        // AI가 생성항 요구사항 정의서 받아옴
        List<CreateRfpResponse> requirements = fetchRequirements(project, member, file, document);

        if (requirements == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();  //생성 시각

        // fast api로부터 받아온 요구사항 정의서 목록들 저장
        for (CreateRfpResponse requirement : requirements) {
            //요구사항 정의서 저장
            Requirement newReq = requirementServiceMapper.toREQEntity(requirement, member, project, now);
            newReq = requirementRepository.save(newReq);

            //출처 저장
            Source source = requirementServiceMapper.toSrcEntity(requirement, newReq, document);
            source = sourceRepository.save(source);
        }
    }
}
