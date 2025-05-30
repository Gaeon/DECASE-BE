package com.skala.decase.domain.project.mapper;

import com.skala.decase.domain.project.controller.dto.response.CreateMemberProjectResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SuccessMapper {
    public CreateMemberProjectResponse success() {
        return new CreateMemberProjectResponse(true);
    }
}
