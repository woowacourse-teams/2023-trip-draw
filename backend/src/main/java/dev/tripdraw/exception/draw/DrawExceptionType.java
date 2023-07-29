package dev.tripdraw.exception.draw;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import dev.tripdraw.exception.common.ExceptionType;
import org.springframework.http.HttpStatus;

public enum DrawExceptionType implements ExceptionType {
    INVALID_COORDINATE(BAD_REQUEST, "Coordinate를 생성할 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    DrawExceptionType(HttpStatus httpStatus, String errorMessage) {
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
