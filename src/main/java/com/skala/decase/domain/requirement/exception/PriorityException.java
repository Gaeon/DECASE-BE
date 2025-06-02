package com.skala.decase.domain.requirement.exception;

import com.skala.decase.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class PriorityException extends CustomException {
    public PriorityException(String message, HttpStatus status) {
        super(message, status);
    }
}
