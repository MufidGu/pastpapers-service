package com.mufidgu.pastpapers.infrastructure.error;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleInvalidInputException(BadRequestException e, WebRequest request) {
        return ProblemDetail.forStatusAndDetail(BadRequestException.httpStatus, e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException e, WebRequest request) {
        return ProblemDetail.forStatusAndDetail(NotFoundException.httpStatus, e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflictException(ConflictException e, WebRequest request) {
        return ProblemDetail.forStatusAndDetail(ConflictException.httpStatus, e.getMessage());
    }

}
