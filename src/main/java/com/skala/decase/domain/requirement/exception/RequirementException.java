package com.skala.decase.domain.requirement.exception;

import com.skala.decase.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class RequirementException extends CustomException {
    public RequirementException(String message, HttpStatus status) {
        super(message, status);
    }
}
