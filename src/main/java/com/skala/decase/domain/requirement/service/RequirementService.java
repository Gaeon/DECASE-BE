package com.skala.decase.domain.requirement.service;

import com.skala.decase.domain.project.domain.Project;
import com.skala.decase.domain.project.exception.ProjectException;
import com.skala.decase.domain.project.repository.ProjectRepository;
import com.skala.decase.domain.requirement.controller.dto.RequirementDto;
import com.skala.decase.domain.requirement.controller.dto.RequirementRevisionDto;
import com.skala.decase.domain.requirement.controller.dto.response.RequirementWithSourceResponse;
import com.skala.decase.domain.requirement.domain.Requirement;
import com.skala.decase.domain.requirement.mapper.RequirementServiceMapper;
import com.skala.decase.domain.requirement.repository.RequirementRepository;
import com.skala.decase.domain.source.domain.Source;
import com.skala.decase.domain.source.service.SourceRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RequirementService {

	private final RequirementRepository requirementRepository;
	private final ProjectRepository projectRepository;
	private final SourceRepository sourceRepository;

	// 기본값 1로 받도록 하기 위함
	public List<RequirementWithSourceResponse> getGeneratedRequirements(Long projectId) {
		return getGeneratedRequirements(projectId, 1);
	}

	public List<RequirementWithSourceResponse> getGeneratedRequirements(Long projectId, int revisionCount) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectException("존재하지 않는 프로젝트 입니다.", HttpStatus.NOT_FOUND));

		//유효한 요구사항 리스트 조회
		List<Requirement> requirements=requirementRepository.findValidRequirementsByProjectAndRevision(project.getProjectId(), revisionCount);
//		System.out.println("조회된 요구사항 개수: " + requirements.size());
		//요구사항이 없는 경우
		if (requirements.isEmpty()) {
			return new ArrayList<>();
		}

		// 유효한 요구사항 PK 목록 추출
		List<Long> reqPks = requirements.stream()
				.map(Requirement::getReqPk)
				.toList();
//		System.out.println("ReqPks: " + reqPks);

		// 해당 req_id_code 들의 특정 버전 이하 모든 요구사항 조회 -> source 용
		List<Requirement> allRelatedReq  = requirementRepository
				.findRequirementsByReqPksAndRevision(reqPks, revisionCount);
//		System.out.println("조회된 관련 요구사항 개수: " + allRelatedReq.size());

		// req_id_code별로 allRelatedReq 그룹화하여 modReason 추출
		Map<String, List<String>> modReasonsByReqIdCode = allRelatedReq.stream()
				.collect(Collectors.groupingBy(
						Requirement::getReqIdCode,
						Collectors.mapping(
								req -> req.getModReason() != null ? req.getModReason() : "",
								Collectors.toList()
						)
				));

		// 특정 버전 이하 모든 요구사항 PK 목록 추출
		List<Long> allRelatedReqPks = allRelatedReq.stream()
				.map(Requirement::getReqPk)
				.toList();

		// 해당 요구사항들의 Source 정보 일괄 조회
		List<Source> sources = sourceRepository.findByRequirementReqPks(allRelatedReqPks );
//		System.out.println("조회된 Source 개수: " + sources.size());
//		sources.forEach(source ->
//				System.out.println("SourceId: " + source.getSourceId() + ", ReqPk: " + source.getRequirement().getReqPk() +
//						", ReqIdCode: " + source.getRequirement().getReqIdCode() + ", DocId: " +
//						(source.getDocument() != null ? source.getDocument().getDocId() : "null"))
//		);

		// 요구사항별로 Source 그룹화
		Map<String, List<Source>> sourcesByReqPk = sources.stream()
				.collect(Collectors.groupingBy(Source::getReqIdCode));
//		System.out.println("=== 5단계: Source 그룹화 결과 ===");
//		sourcesByReqPk.forEach((reqPk, sourceList) ->
//				System.out.println("ReqPk: " + reqPk + " -> Source 개수: " + sourceList.size())
//		);

		// 각 요구사항에 해당하는 Source 리스트 설정
		// 주의: JPA에서 조회된 엔티티의 컬렉션을 직접 수정하는 것은 권장되지 않으므로
		// 새로운 리스트를 생성하여 반환하거나 DTO를 사용하는 것이 좋습니다.
		requirements.forEach(requirement -> {
			List<Source> reqSources = sourcesByReqPk.getOrDefault(requirement.getReqIdCode(), new ArrayList<>());
			// 여기서는 기존 sources 리스트를 clear하고 새로 추가
			requirement.getSources().clear();
			requirement.getSources().addAll(reqSources);
		});
//		System.out.println("=== 최종 결과 ===");
//		System.out.println("반환할 요구사항 개수: " + requirements.size());
//		requirements.forEach(req ->
//				System.out.println("ReqPk: " + req.getReqPk() + ", ReqIdCode: " + req.getReqIdCode() +
//						", Sources 개수: " + req.getSources().size())
//		);

		return requirements.stream()
				.map(requirement -> {
					List<String> modReasons = modReasonsByReqIdCode.getOrDefault(requirement.getReqIdCode(), new ArrayList<>());
					return RequirementServiceMapper.toReqWithSrcResponse(requirement, modReasons);
				})
				.toList();

	}

	public Map<String, List<String>> getRequirementCategory(Long projectId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectException("존재하지 않는 프로젝트 입니다.", HttpStatus.NOT_FOUND));

		List<Requirement> requirements = requirementRepository.findByProject_AndIsDeletedFalse(project);

		Set<String> level1Set = new HashSet<>();
		Set<String> level2Set = new HashSet<>();
		Set<String> level3Set = new HashSet<>();

		for (Requirement req : requirements) {
			if (req.getLevel1() != null) level1Set.add(req.getLevel1());
			if (req.getLevel2() != null) level2Set.add(req.getLevel2());
			if (req.getLevel3() != null) level3Set.add(req.getLevel3());
		}

		Map<String, List<String>> categoryMap = new HashMap<>();
		categoryMap.put("대분류", new ArrayList<>(level1Set));
		categoryMap.put("중분류", new ArrayList<>(level2Set));
		categoryMap.put("소분류", new ArrayList<>(level3Set));

		return categoryMap;
	}

	public List<RequirementDto> getFilteredRequirements(Long projectId, String query,
														String level1, String level2, String level3,
														Integer type, Integer difficulty, Integer priority,
														List<String> docTypes) {

		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectException("존재하지 않는 프로젝트입니다.", HttpStatus.NOT_FOUND));

		List<Requirement> requirements = requirementRepository.findByProject_AndIsDeletedFalse(project);

		// 스트림 필터링
		return requirements.stream()
				.filter(r -> query == null || r.getName().contains(query) || r.getDescription().contains(query))
				.filter(r -> level1 == null || level1.equals(r.getLevel1()))
				.filter(r -> level2 == null || level2.equals(r.getLevel2()))
				.filter(r -> level3 == null || level3.equals(r.getLevel3()))
				.filter(r -> type == null || r.getType().ordinal() == type)
				.filter(r -> difficulty == null || r.getDifficulty().ordinal() == difficulty)
				.filter(r -> priority == null || r.getPriority().ordinal() == priority)
				.filter(r -> docTypes == null || r.getSources().stream()
						.anyMatch(rd -> {
							String docId= rd.getDocument().getDocId();
							String prefix = docId.split("-")[0];
							return docTypes.contains(prefix);
						}))
				.map(RequirementDto::fromEntity)
				.toList();
	}

	// 요구사항 버전 조회
	public List<RequirementRevisionDto> getRequirementRevisions(Long projectId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectException("존재하지 않는 프로젝트입니다.", HttpStatus.NOT_FOUND));

		Integer maxRevision = requirementRepository.getMaxRevisionCount(project);

		String prefix = "요구사항 정의서_";
		int digitCount = String.valueOf(maxRevision).length();
		String format = "%0" + digitCount + "d";

		List<RequirementRevisionDto> versionList = new ArrayList<>();
		for (int i = 1; i <= maxRevision; i++) {
			int finalI = i;
			Optional<Requirement> requirementOpt = requirementRepository.findFirstByProjectAndRevisionCount(project, i);
			requirementOpt.ifPresent(req -> {
				String label = prefix + String.format(format, finalI);
				String date = req.getCreatedDate().toLocalDate().toString();
				versionList.add(new RequirementRevisionDto(label, finalI, date));
			});
		}
		return versionList;
	}
}
