package dev.tripdraw.common.exception;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Collections;
import java.util.List;

public record ExceptionResponse(
        String exceptionCode,
        String message,

        @JsonInclude(NON_EMPTY)
        List<ValidationException> exceptions
) {
    public static ExceptionResponse of(String exceptionCode, String message) {
        return new ExceptionResponse(exceptionCode, message, Collections.emptyList());
    }
}
