package dev.tripdraw.exception.trip;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import dev.tripdraw.exception.common.ExceptionType;
import org.springframework.http.HttpStatus;

public enum TripExceptionType implements ExceptionType {
    TRIP_NOT_FOUND(NOT_FOUND, "존재하지 않는 여행입니다."),
    NOT_AUTHORIZED(FORBIDDEN, "해당 여행에 대한 접근 권한이 없습니다."),
    POINT_NOT_IN_TRIP(NOT_FOUND, "해당 여행에 존재하지 않는 위치정보입니다."),
    POINT_ALREADY_DELETED(CONFLICT, "이미 삭제된 위치정보입니다."),
    POINT_NOT_FOUND(NOT_FOUND, "존재하지 않는 위치입니다."),
    TRIP_INVALID_STATUS(BAD_REQUEST, "유효하지 않은 여행 상태입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    TripExceptionType(HttpStatus httpStatus, String errorMessage) {
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
