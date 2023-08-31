package dev.tripdraw.common.exception;

public class BaseException extends RuntimeException {

    private final ExceptionType exceptionType;

    public BaseException(ExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public ExceptionType exceptionType() {
        return exceptionType;
    }

    @Override
    public String getMessage() {
        return exceptionType.message();
    }
}
