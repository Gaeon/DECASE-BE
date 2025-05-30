package com.skala.decase.domain.member.service;

import com.skala.decase.domain.company.domain.Company;
import com.skala.decase.domain.company.exception.CompanyException;
import com.skala.decase.domain.company.repository.CompanyRepository;
import com.skala.decase.domain.department.domain.Department;
import com.skala.decase.domain.department.exception.DepartmentException;
import com.skala.decase.domain.department.repository.DepartmentRepository;
import com.skala.decase.domain.member.controller.dto.request.LogInRequest;
import com.skala.decase.domain.member.controller.dto.request.SignUpRequest;
import com.skala.decase.domain.member.controller.dto.response.MemberResponse;
import com.skala.decase.domain.member.domain.Member;
import com.skala.decase.domain.member.exception.MemberException;
import com.skala.decase.domain.member.mapper.MemberMapper;
import com.skala.decase.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;

    private final MemberService memberService;

    private final MemberMapper memberMapper;

    /**
     * 회원가입
     *
     * @param request 회원가입 요청 객체
     */
    @Transactional
    public void signUp(SignUpRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new CompanyException("회사를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new DepartmentException("부서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        Member member = memberMapper.toEntity(request, company, department);

        memberRepository.save(member);
    }

    /**
     * 로그인
     *
     * @param request 로그인 요청 객체
     * @return member 사용자 객체
     */
    public MemberResponse login(LogInRequest request) {
        // 아이디로 사용자 찾기
        Member member = memberRepository.findByMemberId(request.id())
                .orElseThrow(() -> new MemberException("해당 사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (!member.getPassword().equals(request.password())) {
            throw new MemberException("비밀번호 불일치", HttpStatus.BAD_REQUEST);
        }

        return memberMapper.toResponse(member);
    }
}
