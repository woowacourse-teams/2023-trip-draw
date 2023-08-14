package dev.tripdraw.exception.member;

import dev.tripdraw.exception.common.ExceptionType;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public enum MemberExceptionType implements ExceptionType {
    DUPLICATE_NICKNAME(CONFLICT, "이미 존재하는 닉네임입니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "존재하지 않는 회원입니다."),
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
