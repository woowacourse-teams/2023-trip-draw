package dev.tripdraw.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public BadRequestException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
