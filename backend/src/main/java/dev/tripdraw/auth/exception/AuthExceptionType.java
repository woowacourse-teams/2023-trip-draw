package dev.tripdraw.auth.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import dev.tripdraw.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

public enum AuthExceptionType implements ExceptionType {
    INVALID_AUTH_HEADER(BAD_REQUEST, "Authoriztion 헤더가 올바르지 않습니다."),
    AUTH_FAIL(FORBIDDEN, "접근 권한이 없습니다."),
    EXPIRED_ACCESS_TOKEN(UNAUTHORIZED, "만료된 엑세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "만료된 리프레시 토큰입니다."),
    INVALID_TOKEN(UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    AuthExceptionType(HttpStatus httpStatus, String errorMessage) {
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
