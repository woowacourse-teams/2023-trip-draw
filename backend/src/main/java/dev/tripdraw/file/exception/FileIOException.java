package dev.tripdraw.file.exception;

import dev.tripdraw.common.exception.BaseException;
import dev.tripdraw.common.exception.ExceptionType;

public class FileIOException extends BaseException {

    public FileIOException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
