package dev.tripdraw.auth.application;

import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.auth.dto.OauthRequest;
import dev.tripdraw.auth.dto.OauthResponse;
import dev.tripdraw.auth.dto.RegisterRequest;
import dev.tripdraw.auth.dto.TokenRefreshRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceFacade {

    private final OAuthService oAuthService;
    private final AuthService authService;

    public OauthResponse login(OauthRequest oauthRequest) {
        OauthInfo oauthInfo = oAuthService.request(oauthRequest.oauthType(), oauthRequest.oauthToken());
        return authService.login(oauthInfo);
    }

    public OauthResponse register(RegisterRequest registerRequest) {
        OauthInfo oauthInfo = oAuthService.request(registerRequest.oauthType(), registerRequest.oauthToken());
        return authService.register(oauthInfo, registerRequest.nickname());
    }

    public OauthResponse refresh(TokenRefreshRequest tokenRefreshRequest) {
        return authService.refresh(tokenRefreshRequest.refreshToken());
    }
}
