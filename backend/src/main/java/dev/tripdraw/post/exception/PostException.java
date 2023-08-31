package dev.tripdraw.post.exception;

import dev.tripdraw.exception.common.BaseException;
import dev.tripdraw.exception.common.ExceptionType;

public class PostException extends BaseException {

    public PostException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
