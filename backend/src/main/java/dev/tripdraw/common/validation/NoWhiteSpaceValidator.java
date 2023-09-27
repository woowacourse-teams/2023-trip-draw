package dev.tripdraw.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class NoWhiteSpaceValidator implements ConstraintValidator<NoWhiteSpace, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !StringUtils.containsWhitespace(value);
    }
}
