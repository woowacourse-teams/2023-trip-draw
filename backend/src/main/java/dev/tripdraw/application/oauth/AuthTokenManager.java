package dev.tripdraw.application.oauth;

import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenManager {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분

    private final JwtTokenProvider jwtTokenProvider;

    public AuthTokenManager(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String generate(Long memberId) {
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String subject = memberId.toString();

        return jwtTokenProvider.generate(subject, accessTokenExpiredAt);
    }

    public Long extractMemberId(String accessToken) {
        return Long.valueOf(jwtTokenProvider.extractSubject(accessToken));
    }
}
