package com.skala.decase.domain.mockup.exception;

import org.springframework.http.HttpStatus;

public class MockupException extends RuntimeException {
    private final HttpStatus status;

    public MockupException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}