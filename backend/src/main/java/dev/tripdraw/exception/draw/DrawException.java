package dev.tripdraw.exception.draw;

import dev.tripdraw.exception.common.BaseException;
import dev.tripdraw.exception.common.ExceptionType;

public class DrawException extends BaseException {

    public DrawException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
