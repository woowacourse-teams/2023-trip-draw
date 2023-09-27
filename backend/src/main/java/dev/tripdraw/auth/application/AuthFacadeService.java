package dev.tripdraw.auth.application;

import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.auth.dto.OauthRequest;
import dev.tripdraw.auth.dto.OauthResponse;
import dev.tripdraw.auth.dto.RegisterRequest;
import dev.tripdraw.auth.dto.TokenRefreshRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
@Service
public class AuthFacadeService {

    private final OAuthService oAuthService;
    private final AuthService authService;
    private final TokenGenerateService tokenGenerateService;
    private final TransactionTemplate transactionTemplate;

    public OauthResponse login(OauthRequest oauthRequest) {
        OauthInfo oauthInfo = oAuthService.request(oauthRequest.oauthType(), oauthRequest.oauthToken());
        return transactionTemplate.execute(status -> authService.login(oauthInfo)
                .map(member -> tokenGenerateService.generate(member.id()))
                .orElseGet(tokenGenerateService::generateEmptyToken));
    }

    public OauthResponse register(RegisterRequest registerRequest) {
        OauthInfo oauthInfo = oAuthService.request(registerRequest.oauthType(), registerRequest.oauthToken());
        return transactionTemplate.execute(status -> authService.register(oauthInfo, registerRequest.nickname())
                .map(member -> tokenGenerateService.generate(member.id()))
                .orElseGet(tokenGenerateService::generateEmptyToken));
    }

    public OauthResponse refresh(TokenRefreshRequest tokenRefreshRequest) {
        return tokenGenerateService.refresh(tokenRefreshRequest.refreshToken());
    }
}
