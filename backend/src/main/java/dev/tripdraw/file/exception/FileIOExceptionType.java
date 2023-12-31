package dev.tripdraw.file.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import dev.tripdraw.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

public enum FileIOExceptionType implements ExceptionType {
    FILE_SAVE_FAIL(INTERNAL_SERVER_ERROR, "파일 저장에 실패했습니다."),
    INVALID_CONTENT_TYPE(BAD_REQUEST, "지원하지 않는 파일 형식입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    FileIOExceptionType(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String message() {
        return errorMessage;
    }
}
