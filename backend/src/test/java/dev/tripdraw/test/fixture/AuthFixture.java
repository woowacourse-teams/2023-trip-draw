package dev.tripdraw.test.fixture;

import static dev.tripdraw.common.auth.OauthType.KAKAO;

import dev.tripdraw.auth.config.AccessTokenConfig;
import dev.tripdraw.auth.config.RefreshTokenConfig;
import dev.tripdraw.auth.dto.OauthInfo;

public class AuthFixture {

    private static final long INVALID_TOKEN_EXPIRE_TIME = -180000L;
    private static final String ACCESS_TOKEN_KEY =
            "ACCESSTOKENACCESSTOKENACCESSTOKENACCESSTOKENACCESSTOKENACCESSTOKENACCESSTOKENACCESSTOKEN";
    private static final String REFRESH_TOKEN_KEY =
            "REFRESHTOKENREFRESHTOKENREFRESHTOKENREFRESHTOKENREFRESHTOKENREFRESHTOKENREFRESHTOKENREFR";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;

    public static AccessTokenConfig 테스트_ACCESS_TOKEN_설정() {
        return new AccessTokenConfig(ACCESS_TOKEN_KEY, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public static AccessTokenConfig 만료된_토큰_생성용_ACCESS_TOKEN_설정() {
        return new AccessTokenConfig(ACCESS_TOKEN_KEY, INVALID_TOKEN_EXPIRE_TIME);
    }

    public static RefreshTokenConfig 테스트_REFRESH_TOKEN_설정() {
        return new RefreshTokenConfig(REFRESH_TOKEN_KEY, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public static RefreshTokenConfig 만료된_토큰_생성용_REFRESH_TOKEN_설정() {
        return new RefreshTokenConfig(REFRESH_TOKEN_KEY, INVALID_TOKEN_EXPIRE_TIME);
    }

    public static OauthInfo OAuth_정보() {
        return new OauthInfo("id", KAKAO);
    }
}
