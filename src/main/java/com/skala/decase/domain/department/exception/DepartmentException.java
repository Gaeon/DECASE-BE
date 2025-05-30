package com.skala.decase.domain.department.exception;

import com.skala.decase.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class DepartmentException extends CustomException {
    public DepartmentException(String message, HttpStatus status) {
        super(message, status);
    }
}
