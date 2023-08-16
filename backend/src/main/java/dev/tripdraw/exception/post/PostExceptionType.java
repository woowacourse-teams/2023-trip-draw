package dev.tripdraw.exception.post;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import dev.tripdraw.exception.common.ExceptionType;
import org.springframework.http.HttpStatus;

public enum PostExceptionType implements ExceptionType {
    POST_NOT_FOUNT(NOT_FOUND, "존재하지 않는 감상입니다."),
    NOT_AUTHORIZED_TO_POST(FORBIDDEN, "해당 감상에 대한 접근 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;

    PostExceptionType(HttpStatus httpStatus, String errorMessage) {
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
