package dev.tripdraw.config;

import dev.tripdraw.presentation.member.AuthArgumentResolver;
import dev.tripdraw.presentation.member.AuthExtractor;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final AuthExtractor authExtractor;

    public AuthConfig(AuthExtractor authExtractor) {
        this.authExtractor = authExtractor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(authExtractor));
    }
}
