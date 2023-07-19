package dev.tripdraw.exception.member;

import static org.springframework.http.HttpStatus.CONFLICT;

import dev.tripdraw.exception.common.ExceptionType;
import org.springframework.http.HttpStatus;

public enum MemberExceptionType implements ExceptionType {
    NICKNAME_CONFLICT(CONFLICT, "이미 존재하는 닉네임입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    MemberExceptionType(HttpStatus httpStatus, String errorMessage) {
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
