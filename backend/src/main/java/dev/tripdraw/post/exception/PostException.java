package dev.tripdraw.post.exception;

import dev.tripdraw.common.exception.BaseException;
import dev.tripdraw.common.exception.ExceptionType;

public class PostException extends BaseException {

    public PostException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
