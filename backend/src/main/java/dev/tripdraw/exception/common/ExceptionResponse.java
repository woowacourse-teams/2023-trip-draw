package dev.tripdraw.exception.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Collections;
import java.util.List;

public record ExceptionResponse(
        String exceptionCode,
        String message,

        @JsonInclude(Include.NON_EMPTY)
        List<ValidationException> exceptions
) {
    public static ExceptionResponse of(String exceptionCode, String message) {
        return new ExceptionResponse(exceptionCode, message, Collections.emptyList());
    }
}
