package dev.tripdraw.exception.auth;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import dev.tripdraw.exception.common.ExceptionType;
import org.springframework.http.HttpStatus;

public enum AuthExceptionType implements ExceptionType {
    NO_AUTH_HEADER(BAD_REQUEST, "Auth 헤더가 없습니다."),
    AUTH_FAIL(FORBIDDEN, "접근 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    AuthExceptionType(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
