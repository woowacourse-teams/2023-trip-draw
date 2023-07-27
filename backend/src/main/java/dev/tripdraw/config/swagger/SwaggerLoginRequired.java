package dev.tripdraw.config.swagger;

import static dev.tripdraw.config.swagger.SwaggerConfig.BEARER_SECURITY_SCHEME_KEY;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@SecurityRequirement(name = BEARER_SECURITY_SCHEME_KEY)
public @interface SwaggerLoginRequired {
}
