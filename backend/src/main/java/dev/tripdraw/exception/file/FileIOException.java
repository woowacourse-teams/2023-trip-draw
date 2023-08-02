package dev.tripdraw.exception.file;

import dev.tripdraw.exception.common.BaseException;
import dev.tripdraw.exception.common.ExceptionType;

public class FileIOException extends BaseException {

    public FileIOException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
