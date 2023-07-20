package dev.tripdraw.config;

import dev.tripdraw.presentation.member.AuthArgumentResolver;
import dev.tripdraw.presentation.member.AuthExtractor;
import dev.tripdraw.presentation.member.AuthInterceptor;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final AuthExtractor authExtractor;
    private final AuthInterceptor authInterceptor;

    public AuthConfig(AuthExtractor authExtractor, AuthInterceptor authInterceptor) {
        this.authExtractor = authExtractor;
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(authExtractor));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/members");
    }
}
