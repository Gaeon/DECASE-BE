package com.skala.decase.domain.project.mapper;

import com.skala.decase.domain.project.controller.dto.response.CreateMemberProjectResponse;
import com.skala.decase.domain.project.controller.dto.response.JoinProjectResponse;
import com.skala.decase.domain.project.domain.ProjectInvitation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SuccessMapper {
    public CreateMemberProjectResponse success() {
        return new CreateMemberProjectResponse(true);
    }

    public JoinProjectResponse isJoinSuccess(boolean result, ProjectInvitation projectInvitation) {
        if (result) {
            return new JoinProjectResponse(result, "프로젝트 참여가 완료되었습니다.", null, null);
        }
        return new JoinProjectResponse(result, "아직 회원이 아닙니다. 회원 가입 진행 후 다시 초대 링크를 클릭해 주세요.", projectInvitation.getEmail(), projectInvitation.getToken());
    }
}
