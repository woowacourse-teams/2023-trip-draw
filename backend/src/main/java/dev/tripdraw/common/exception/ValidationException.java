package dev.tripdraw.common.exception;

import org.springframework.validation.FieldError;

public record ValidationException(String field, String message) {

    public static ValidationException from(FieldError fieldError) {
        return new ValidationException(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
