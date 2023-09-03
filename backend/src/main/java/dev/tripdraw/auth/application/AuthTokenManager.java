package dev.tripdraw.auth.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenManager {

    private final JwtTokenProvider jwtTokenProvider;
    private final Long expirationTime;

    public AuthTokenManager(JwtTokenProvider jwtTokenProvider,
                            @Value("${jwt.access.expiration-time}") Long expirationTime) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.expirationTime = expirationTime;
    }

    public String generate(Long memberId) {
        String subject = memberId.toString();
        return jwtTokenProvider.generateAccessToken(subject);
    }

    public Long extractMemberId(String accessToken) {
        return Long.valueOf(jwtTokenProvider.extractAccessToken(accessToken));
    }
}
