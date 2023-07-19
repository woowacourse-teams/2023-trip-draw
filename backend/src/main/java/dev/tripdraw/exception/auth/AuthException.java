package dev.tripdraw.exception.auth;

import dev.tripdraw.exception.common.BaseException;
import dev.tripdraw.exception.common.ExceptionType;

public class AuthException extends BaseException {

    public AuthException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
