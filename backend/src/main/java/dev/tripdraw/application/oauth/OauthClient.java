package dev.tripdraw.application.oauth;

import dev.tripdraw.domain.oauth.OauthType;
import dev.tripdraw.dto.auth.OauthInfo;

public interface OauthClient {

    OauthType oauthType();
    
    OauthInfo requestOauthInfo(String accessToken);
}
