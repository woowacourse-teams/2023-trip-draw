package dev.tripdraw.exception.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseException extends RuntimeException {

    private final ExceptionType exceptionType;

    public ExceptionType getExceptionType() {
        return exceptionType;
    }
}
