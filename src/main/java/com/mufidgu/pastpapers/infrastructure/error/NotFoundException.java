package com.mufidgu.pastpapers.infrastructure.error;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {
    public static HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    public NotFoundException(String message) {
        super(message);
    }
}