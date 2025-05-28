package com.skala.decase.domain.doc_type.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DocTypeException extends RuntimeException {
    private final HttpStatus status;

    public DocTypeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}