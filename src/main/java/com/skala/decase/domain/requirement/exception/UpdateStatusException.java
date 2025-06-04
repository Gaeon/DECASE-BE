package com.skala.decase.domain.requirement.exception;

import com.skala.decase.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UpdateStatusException extends CustomException {
    public UpdateStatusException(String message, HttpStatus status) {
        super(message, status);
    }
}
