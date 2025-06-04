package com.skala.decase.domain.requirement.domain;

import com.skala.decase.domain.requirement.exception.UpdateStatusException;
import org.springframework.http.HttpStatus;

public enum UpdateStatus {
    CREATE, UPDATE, DELETE;

    public static UpdateStatus fromAI(String value) {
        return switch (value) {
            case "create" -> CREATE;
            case "update" -> UPDATE;
            case "delete" -> DELETE;
            default -> throw new UpdateStatusException("Unknown update status value: " + value, HttpStatus.BAD_REQUEST);
        };
    }
}
