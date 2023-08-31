package dev.tripdraw.auth.oauth;

import dev.tripdraw.auth.domain.OauthType;
import dev.tripdraw.auth.dto.OauthInfo;

public interface OauthClient {

    OauthType oauthType();

    OauthInfo requestOauthInfo(String accessToken);
}
