package dev.tripdraw.exception.file;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import dev.tripdraw.exception.common.ExceptionType;
import org.springframework.http.HttpStatus;

public enum FileIOExceptionType implements ExceptionType {
    FILE_SAVE_FAIL(INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    FileIOExceptionType(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
