package dev.tripdraw.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public ForbiddenException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
