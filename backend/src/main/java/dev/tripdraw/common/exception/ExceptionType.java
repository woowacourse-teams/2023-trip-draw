package dev.tripdraw.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionType {

    String name();

    HttpStatus httpStatus();

    String message();
}
