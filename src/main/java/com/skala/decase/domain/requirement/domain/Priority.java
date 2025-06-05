package com.skala.decase.domain.requirement.domain;

import com.skala.decase.domain.requirement.exception.PriorityException;
import org.springframework.http.HttpStatus;

public enum Priority {
    HIGH, MIDDLE, LOW;

    public static Priority fromKorean(String value) {
        return switch (value) {
            case "상" -> HIGH;
            case "중" -> MIDDLE;
            case "하" -> LOW;
            default -> throw new PriorityException("Unknown priority value: " + value, HttpStatus.BAD_REQUEST);
        };
    }
}

