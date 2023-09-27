package dev.tripdraw.auth.exception;

import dev.tripdraw.common.exception.BaseException;
import dev.tripdraw.common.exception.ExceptionType;

public class AuthException extends BaseException {

    public AuthException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
