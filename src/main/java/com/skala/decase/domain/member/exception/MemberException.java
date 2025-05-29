package com.skala.decase.domain.member.exception;

import com.skala.decase.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class MemberException extends CustomException {
    public MemberException(String message, HttpStatus status) {
        super(message, status);
    }
}
