package dev.tripdraw.common;

import static dev.tripdraw.auth.domain.OauthType.KAKAO;

import dev.tripdraw.auth.oauth.OauthClient;
import dev.tripdraw.auth.domain.OauthType;
import dev.tripdraw.auth.dto.OauthInfo;

public class TestKakaoApiClient implements OauthClient {

    @Override
    public OauthType oauthType() {
        return KAKAO;
    }

    @Override
    public OauthInfo requestOauthInfo(String accessToken) {
        return new OauthInfo("kakaoId", KAKAO);
    }
}
