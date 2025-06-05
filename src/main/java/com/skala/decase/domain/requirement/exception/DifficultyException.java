package com.skala.decase.domain.requirement.exception;

import com.skala.decase.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DifficultyException extends CustomException {
    public DifficultyException(String message, HttpStatus status) {
        super(message, status);
    }
}
