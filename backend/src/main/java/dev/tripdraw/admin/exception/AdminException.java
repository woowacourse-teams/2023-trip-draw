package dev.tripdraw.admin.exception;

import dev.tripdraw.common.exception.BaseException;
import dev.tripdraw.common.exception.ExceptionType;

public class AdminException extends BaseException {

    public AdminException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
