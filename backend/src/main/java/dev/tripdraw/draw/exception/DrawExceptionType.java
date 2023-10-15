package dev.tripdraw.draw.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import dev.tripdraw.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

public enum DrawExceptionType implements ExceptionType {
    INVALID_COORDINATES(BAD_REQUEST, "Coordinates를 생성할 수 없습니다."),
    IMAGE_SAVE_FAIL(BAD_REQUEST, "이미지를_저장할_수_없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    DrawExceptionType(HttpStatus httpStatus, String errorMessage) {
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
