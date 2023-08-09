package dev.tripdraw.common;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;

import dev.tripdraw.application.oauth.OauthClient;
import dev.tripdraw.domain.oauth.OauthType;
import dev.tripdraw.dto.auth.OauthInfo;

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
