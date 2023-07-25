package dev.tripdraw.config.swagger;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(
        in = HEADER,
        name = "Authorization",
        description = "Authorization: BASE64(nickname) / default : 인코딩 된 \"통후추\"",
        schema = @Schema(type = "string", defaultValue = "7Ya17ZuE7LaU"))
public @interface SwaggerLoginRequired {
}
