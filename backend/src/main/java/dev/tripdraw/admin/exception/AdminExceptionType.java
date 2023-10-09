package dev.tripdraw.admin.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import dev.tripdraw.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

public enum AdminExceptionType implements ExceptionType {
    ADMIN_AUTH_FAIL(BAD_REQUEST, "인증에 실패했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    AdminExceptionType(HttpStatus httpStatus, String errorMessage) {
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
