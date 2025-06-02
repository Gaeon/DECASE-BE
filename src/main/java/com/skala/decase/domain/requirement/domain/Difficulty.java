package com.skala.decase.domain.requirement.domain;

import com.skala.decase.domain.requirement.exception.DifficultyException;
import org.springframework.http.HttpStatus;

public enum Difficulty {
    HIGH, MIDDLE, LOW;

    public static Difficulty fromKorean(String value) {
        return switch (value) {
            case "상" -> HIGH;
            case "중" -> MIDDLE;
            case "하" -> LOW;
            default -> throw new DifficultyException("Unknown difficulty value: " + value, HttpStatus.BAD_REQUEST);
        };
    }
}
