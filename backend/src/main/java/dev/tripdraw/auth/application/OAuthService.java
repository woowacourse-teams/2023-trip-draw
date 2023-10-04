package dev.tripdraw.auth.application;

import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.auth.oauth.OauthClient;
import dev.tripdraw.auth.oauth.OauthClientProvider;
import dev.tripdraw.common.auth.OauthType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuthService {

    private final OauthClientProvider oauthClientProvider;

    public OauthInfo request(OauthType oauthType, String oauthToken) {
        OauthClient oauthClient = oauthClientProvider.provide(oauthType);
        return oauthClient.requestOauthInfo(oauthToken);
    }
}
