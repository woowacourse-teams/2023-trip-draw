package dev.tripdraw.exception.common;

import org.springframework.http.HttpStatus;

public interface ExceptionType {

    String name();

    HttpStatus httpStatus();

    String message();
}
