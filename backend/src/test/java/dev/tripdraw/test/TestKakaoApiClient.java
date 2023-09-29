package dev.tripdraw.test;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.test.fixture.AuthFixture.OAuth_정보;

import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.auth.oauth.OauthClient;
import dev.tripdraw.common.auth.OauthType;

public class TestKakaoApiClient implements OauthClient {

    @Override
    public OauthType oauthType() {
        return KAKAO;
    }

    @Override
    public OauthInfo requestOauthInfo(String accessToken) {
        return OAuth_정보();
    }
}
