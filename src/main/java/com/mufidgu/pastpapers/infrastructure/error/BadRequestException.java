package com.mufidgu.pastpapers.infrastructure.error;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RuntimeException {
    public static HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    public BadRequestException(String message) {
        super(message);
    }
}