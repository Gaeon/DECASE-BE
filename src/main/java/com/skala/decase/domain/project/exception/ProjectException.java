package com.skala.decase.domain.project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProjectException extends RuntimeException {
    private final HttpStatus status;

    public ProjectException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}