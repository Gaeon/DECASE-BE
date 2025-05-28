package com.skala.decase.domain.company.exception;

import com.skala.decase.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CompanyException extends CustomException {
    public CompanyException(String message, HttpStatus status) {
        super(message, status);
    }
}
