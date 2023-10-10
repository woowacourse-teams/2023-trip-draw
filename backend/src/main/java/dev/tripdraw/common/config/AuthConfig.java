package dev.tripdraw.common.config;

import dev.tripdraw.auth.application.AuthExtractor;
import dev.tripdraw.auth.presentation.AuthArgumentResolver;
import dev.tripdraw.auth.presentation.AuthInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final AuthExtractor authExtractor;
    private final AuthInterceptor authInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(authExtractor));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/api-docs")
                .excludePathPatterns("/v3/api-docs/**")
                .excludePathPatterns("/oauth/**")
                .excludePathPatterns("/points/{\\d+}/post");
    }
}
