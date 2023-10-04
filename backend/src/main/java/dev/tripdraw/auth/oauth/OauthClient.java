package dev.tripdraw.auth.oauth;

import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.common.auth.OauthType;

public interface OauthClient {

    OauthType oauthType();

    OauthInfo requestOauthInfo(String accessToken);
}
