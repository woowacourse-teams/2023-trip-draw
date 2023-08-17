package dev.tripdraw.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String BEARER_TYPE = "bearer";
    static final String BEARER_SECURITY_SCHEME_KEY = "Authorization Bearer";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addServersItem(url())
                .components(authorization())
                .info(info());
    }

    private Server url() {
        return new Server()
                .url("/");
    }

    private Components authorization() {
        return new Components()
                .addSecuritySchemes(BEARER_SECURITY_SCHEME_KEY, securityScheme());
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme(BEARER_TYPE)
                .description("Base64로 인코딩된 닉네임을 입력해주세요. 통후추 예시) 7Ya17ZuE7LaU");
    }

    private Info info() {
        return new Info()
                .title("트립드로우 서버 API 명세서")
                .version("v0.0.1")
                .description("API Description");
    }
}
