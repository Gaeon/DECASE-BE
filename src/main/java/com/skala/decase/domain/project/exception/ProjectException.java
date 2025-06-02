package com.skala.decase.domain.project.exception;

import com.skala.decase.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ProjectException extends CustomException {
    public ProjectException(String message, HttpStatus status) {
        super(message, status);
    }
}
