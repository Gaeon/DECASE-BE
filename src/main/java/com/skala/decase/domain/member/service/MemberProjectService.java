package com.skala.decase.domain.member.service;

import com.skala.decase.domain.member.controller.dto.response.MemberProjectListResponse;
import com.skala.decase.domain.member.domain.Member;
import com.skala.decase.domain.member.repository.MemberProjectRepository;
import com.skala.decase.domain.project.domain.MemberProject;
import com.skala.decase.domain.project.domain.ProjectStatus;
import com.skala.decase.domain.project.mapper.MemberProjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberProjectService {

    private final MemberProjectRepository memberProjectRepository;
    private final MemberService memberService;
    private final MemberProjectMapper memberProjectMapper;

    /**
     * 회원별 프로젝트 목록 조회 (페이징)
     */
    public List<MemberProjectListResponse> getProjectsByMember(Long memberId, String projectName, ProjectStatus status,
                                                               String proposalPM, Pageable pageable) {
        // 회원 존재 확인
        Member member = memberService.findByMemberId(memberId);

        // 회원의 프로젝트 목록 조회

        // 필터 조건에 따라 적절한 Repository 메서드 호출
        Page<MemberProject> memberProjectsPage = findMemberProjectsByFilter(memberId, projectName, status, proposalPM,
                pageable);

        return memberProjectsPage.stream()
                .map(memberProjectMapper::toListResponse)
                .toList();
    }

    /**
     * 필터 조건에 따라 적절한 Repository 메서드 선택 : 이거 곧 리팩토링 할게...... 지금은 할게 많으니..
     */
    private Page<MemberProject> findMemberProjectsByFilter(Long memberId, String projectName, ProjectStatus status,
                                                           String proposalPM, Pageable pageable) {
        boolean hasProjectName = StringUtils.hasText(projectName);
        boolean hasStatus = status != null;
        boolean hasProposalPM = StringUtils.hasText(proposalPM);

        // 필터 조건 조합에 따라 적절한 메서드 호출
        if (hasProjectName && hasStatus && hasProposalPM) {
            // 모든 필터 적용
            return memberProjectRepository.findByMemberIdAndAllFilters(memberId, projectName, status, proposalPM,
                    pageable);
        } else if (hasProjectName && hasStatus) {
            // 프로젝트명 + 상태
            return memberProjectRepository.findByMemberIdAndProjectNameAndStatus(memberId, projectName, status,
                    pageable);
        } else if (hasProjectName && hasProposalPM) {
            // 프로젝트명 + 제안 PM
            return memberProjectRepository.findByMemberIdAndProjectNameAndProposalPM(memberId, projectName, proposalPM,
                    pageable);
        } else if (hasStatus && hasProposalPM) {
            // 상태 + 제안 PM
            return memberProjectRepository.findByMemberIdAndStatusAndProposalPM(memberId, status, proposalPM, pageable);
        } else if (hasProjectName) {
            // 프로젝트명만
            return memberProjectRepository.findByMemberIdAndProjectName(memberId, projectName, pageable);
        } else if (hasStatus) {
            // 상태만
            return memberProjectRepository.findByMemberIdAndStatus(memberId, status, pageable);
        } else if (hasProposalPM) {
            // 제안 PM만
            return memberProjectRepository.findByMemberIdAndProposalPM(memberId, proposalPM, pageable);
        } else {
            // 필터 없음
            return memberProjectRepository.findByMemberIdWithProject(memberId, pageable);
        }
    }
}
