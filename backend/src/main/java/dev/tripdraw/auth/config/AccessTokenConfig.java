package dev.tripdraw.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt.access")
public record AccessTokenConfig(
        String secretKey,
        Long expirationTime
) {
}
