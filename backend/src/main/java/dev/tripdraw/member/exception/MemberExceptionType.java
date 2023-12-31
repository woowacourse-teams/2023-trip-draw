package dev.tripdraw.member.exception;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import dev.tripdraw.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

public enum MemberExceptionType implements ExceptionType {
    DUPLICATE_NICKNAME(CONFLICT, "이미 존재하는 닉네임입니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "존재하지 않는 회원입니다."),
    MEMBER_NOT_REGISTERED(INTERNAL_SERVER_ERROR, "가입이 완료되지 않은 회원이 존재합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    MemberExceptionType(HttpStatus httpStatus, String errorMessage) {
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
