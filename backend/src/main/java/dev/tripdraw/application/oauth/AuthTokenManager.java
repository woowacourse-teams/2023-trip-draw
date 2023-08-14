package dev.tripdraw.application.oauth;

import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenManager {

    private final JwtTokenProvider jwtTokenProvider;
    private final Long expirationTime;

    public AuthTokenManager(JwtTokenProvider jwtTokenProvider, @Value("${jwt.expiration-time}") Long expirationTime) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.expirationTime = expirationTime;
    }

    public String generate(Long memberId) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + expirationTime);

        String subject = memberId.toString();

        return jwtTokenProvider.generate(subject, accessTokenExpiredAt);
    }

    public Long extractMemberId(String accessToken) {
        return Long.valueOf(jwtTokenProvider.extractSubject(accessToken));
    }
}
