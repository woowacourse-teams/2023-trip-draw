package dev.tripdraw.test.fixture;

import dev.tripdraw.auth.config.AccessTokenConfig;
import dev.tripdraw.auth.config.RefreshTokenConfig;

public class AuthFixture {

    public static final String 유효하지_않은_토큰 = "Invalid.Token.XD";
    private static final long INVALID_TOKEN_EXPIRE_TIME = -180000L;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;
    private static final String TOKEN =
            "TOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKEN";

    public static AccessTokenConfig 테스트_ACCESS_TOKEN_설정() {
        return new AccessTokenConfig(TOKEN, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public static AccessTokenConfig 만료된_토큰_생성용_ACCESS_TOKEN_설정() {
        return new AccessTokenConfig(TOKEN, INVALID_TOKEN_EXPIRE_TIME);
    }

    public static RefreshTokenConfig 테스트_REFRESH_TOKEN_설정() {
        return new RefreshTokenConfig(TOKEN, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public static RefreshTokenConfig 만료된_토큰_생성용_REFRESH_TOKEN_설정() {
        return new RefreshTokenConfig(TOKEN, INVALID_TOKEN_EXPIRE_TIME);
    }
}
