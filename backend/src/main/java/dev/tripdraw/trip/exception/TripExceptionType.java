package dev.tripdraw.trip.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import dev.tripdraw.common.exception.ExceptionType;
import org.springframework.http.HttpStatus;

public enum TripExceptionType implements ExceptionType {
    TRIP_NOT_FOUND(NOT_FOUND, "존재하지 않는 여행입니다."),
    NOT_AUTHORIZED_TO_TRIP(FORBIDDEN, "해당 여행에 대한 접근 권한이 없습니다."),
    POINT_ALREADY_DELETED(CONFLICT, "이미 삭제된 위치정보입니다."),
    POINT_NOT_FOUND(NOT_FOUND, "존재하지 않는 위치입니다."),
    TRIP_INVALID_STATUS(BAD_REQUEST, "잘못된 여행 상태입니다."),
    POINT_ALREADY_HAS_POST(CONFLICT, "이미 감상이 등록된 위치입니다."),
    TRIP_ALREADY_DELETED(CONFLICT, "이미 삭제된 여행입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    TripExceptionType(HttpStatus httpStatus, String errorMessage) {
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
