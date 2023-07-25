package dev.tripdraw.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        final Info info = createInfo();

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .components(
                        new Components()
                                .addHeaders("Authorization", createCustomizedHeader()))
                .info(info);
    }

    private Info createInfo() {
        final Info info = new Info()
                .title("트립드로우 서버 API 명세서")
                .version("v0.0.1")
                .description("API Description");
        return info;
    }

    private Header createCustomizedHeader() {
        Header customHeader = new Header();
        customHeader.setDescription("Authorization : BASE64(nickname)");
        customHeader.schema(new StringSchema());
        return customHeader;
    }
}
