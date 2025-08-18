package com.mufidgu.pastpapers.infrastructure.error;

import org.springframework.http.HttpStatus;

public class ConflictException extends RuntimeException {
    public static HttpStatus httpStatus = HttpStatus.CONFLICT;
    public ConflictException(String message) {
        super(message);
    }
}