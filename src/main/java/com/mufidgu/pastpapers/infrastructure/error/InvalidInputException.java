package com.mufidgu.pastpapers.infrastructure.error;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}