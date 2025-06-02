package com.skala.decase.domain.requirement.domain;

import com.skala.decase.domain.requirement.exception.RequirementTypeException;
import org.springframework.http.HttpStatus;

public enum RequirementType {
    FR, NFR;

    public static RequirementType fromKorean(String value) {
        return switch (value) {
            case "기능적" -> FR;
            case "비기능적" -> NFR;
            default -> throw new RequirementTypeException("Unknown requirement type value: " + value,
                    HttpStatus.BAD_REQUEST);
        };
    }
}
