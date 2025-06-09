package com.skala.decase.domain.requirement.service.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SrsAgentResponse {
    private String message;
    private List<CreateRfpResponse> requirements;

} 