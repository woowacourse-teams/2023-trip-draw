package dev.tripdraw.post.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import dev.tripdraw.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

public enum PostExceptionType implements ExceptionType {
    POST_NOT_FOUND(NOT_FOUND, "존재하지 않는 감상입니다."),
    NOT_AUTHORIZED_TO_POST(FORBIDDEN, "해당 감상에 대한 접근 권한이 없습니다."),
    ;

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
