package com.skala.decase.domain.project.service;

import com.skala.decase.domain.document.domain.Document;
import com.skala.decase.domain.member.domain.Member;
import com.skala.decase.domain.member.service.MemberService;
import com.skala.decase.domain.project.controller.dto.request.CreateProjectRequest;
import com.skala.decase.domain.project.controller.dto.response.*;
import com.skala.decase.domain.project.domain.MemberProject;
import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.project.domain.ProjectInvitation;
import com.skala.decase.domain.project.exception.ProjectException;
import com.skala.decase.domain.project.mapper.MemberProjectMapper;
import com.skala.decase.domain.project.mapper.ProjectMapper;
import com.skala.decase.domain.member.repository.MemberProjectRepository;
import com.skala.decase.domain.project.mapper.SuccessMapper;
import com.skala.decase.domain.project.repository.ProjectInvitationRepository;
import com.skala.decase.domain.project.repository.ProjectRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.skala.decase.domain.requirement.domain.Requirement;
import com.skala.decase.domain.requirement.domain.RequirementDocument;
import com.skala.decase.domain.requirement.repository.RequirementDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MemberProjectRepository memberProjectRepository;
    private final ProjectInvitationRepository projectInvitationRepository;
    private final RequirementDocumentRepository requirementDocumentRepository;

    private final MemberService memberService;

    private final ProjectMapper projectMapper;
    private final MemberProjectMapper memberProjectMapper;
    private final SuccessMapper successMapper;

    /**
     * 프로젝트 존재 확인
     */
    public Project findByProjectId(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException("존재하지 않는 프로젝트입니다.", HttpStatus.NOT_FOUND));
    }

    /**
     * 프로젝트 생성
     *
     * @param request
     * @return
     */
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request) {
        validateProjectCreateRequest(request);

        //프로젝트 생성한 사용자 찾기
        Member creator = memberService.findByMemberId(request.creatorMemberId());
        //프로젝트 생성
        LocalDateTime now = LocalDateTime.now();
        Project project = projectMapper.toInitialEntity(request, now);
        Project savedProject = projectRepository.save(project);

        //프로젝트-생성한 사용자 관리
        MemberProject memberProject = memberProjectMapper.toAdminEntity(creator, savedProject);
        memberProjectRepository.save(memberProject);

        return projectMapper.toResponse(savedProject, creator);
    }

    /**
     * 프로젝트 생성 유효성 검증
     *
     * @param request
     */
    private void validateProjectCreateRequest(CreateProjectRequest request) {
        // 종료일이 시작일보다 이후인지 검증
        if (request.endDate().before(request.startDate())) {
            throw new ProjectException("종료일은 시작일 이후여야 합니다.", HttpStatus.BAD_REQUEST);
        }

        // 프로젝트 규모 검증 (음수 불가)
        if (request.scale() < 0) {
            throw new ProjectException("프로젝트 규모는 0 이상이어야 합니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // 프로젝트 수정
    @Transactional
    public EditProjectResponseDto editProject(Long projectId, CreateProjectRequest request) {
        // 프로젝트 존재 확인
        Project editProject = findByProjectId(projectId);
        // 유효성 검증
        validateProjectCreateRequest(request);

        editProject.setName(request.name());
        editProject.setProposalPM(request.proposalPM());
        editProject.setDescription(request.description());
        editProject.setScale(request.scale());
        editProject.setStartDate(request.startDate());
        editProject.setEndDate(request.endDate());
        editProject.setModifiedDate(LocalDateTime.now()); // 수정 시각 갱신

        Project saved = projectRepository.save(editProject);
        return EditProjectResponseDto.fromEntity(saved);
    }

    @Transactional
    public DeleteProjectResponse deleteProject(Long projectId) {
        Project project = findByProjectId(projectId);

        projectInvitationRepository.deleteByProject_ProjectId(projectId);
        memberProjectRepository.deleteByProject_ProjectId(projectId);
        projectRepository.delete(project);
        return successMapper.toDelete();
    }

    // 단일 프로젝트 상세 설명
    public ProjectDetailResponseDto getProject(Long projectId) {
        Project project = findByProjectId(projectId);

        return new ProjectDetailResponseDto(
                project.getProjectId(),
                project.getName(),
                project.getScale(),
                project.getStartDate(),
                project.getEndDate(),
                project.getDescription(),
                project.getProposalPM()
        );
    }

    private String getDocName(Requirement requirement) {
        Document document = requirementDocumentRepository.findByRequirement(requirement).getDocument();
        return document.getName();
    }

    private int getPageNum(Requirement requirement) {
        RequirementDocument requirementDocument = requirementDocumentRepository.findByRequirement(requirement);
        return requirementDocument.getPageNum();
    }

    private String getRelSentence(Requirement requirement) {
        RequirementDocument requirementDocument = requirementDocumentRepository.findByRequirement(requirement);
        return requirementDocument.getRelSentence();
    }

    // 조견표 리스트 생성
    private List<MappingTableResponseDto> createMappingTable(Long projectId) {
        Project project = findByProjectId(projectId);
        List<MappingTableResponseDto> responseList = project.getRequirements().stream()
                .filter(req -> !req.isDeleted())
                .map(req -> new MappingTableResponseDto(
                        req.getReqIdCode(),
                        req.getName(),
                        req.getDescription(),
                        getDocName(req),
                        getPageNum(req),
                        getRelSentence(req)
                ))
                .toList();

        return responseList;
    }

    // 조견표 출력
    public byte[] exportMappingTableToExcel(Long projectId) throws IOException {
        List<MappingTableResponseDto> list = createMappingTable(projectId);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Mapping Table");

        // 1. 헤더 작성
        Row headerRow = sheet.createRow(0);
        String[] headers = {"요구사항 ID", "요구사항명", "설명", "출처 문서명", "페이지 번호", "관련 문장"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // 2. 내용 채우기
        int rowIdx = 1;
        for (MappingTableResponseDto dto : list) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(dto.req_code());
            row.createCell(1).setCellValue(dto.name());
            row.createCell(2).setCellValue(dto.description());
            row.createCell(3).setCellValue(dto.docName());
            row.createCell(4).setCellValue(dto.pageNum());
            row.createCell(5).setCellValue(dto.relSentence());
        }

        // 3. 엑셀 파일을 byte[]로 변환 (서버에서 다운로드 응답용)
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }
}
