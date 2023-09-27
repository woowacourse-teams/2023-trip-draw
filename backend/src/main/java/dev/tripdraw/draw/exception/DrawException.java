package dev.tripdraw.draw.exception;

import dev.tripdraw.common.exception.BaseException;
import dev.tripdraw.common.exception.ExceptionType;

public class DrawException extends BaseException {

    public DrawException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
