package dev.tripdraw.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public ConflictException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
