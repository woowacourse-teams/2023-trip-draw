package dev.tripdraw.auth.application;

import static dev.tripdraw.auth.exception.AuthExceptionType.INVALID_TOKEN;

import dev.tripdraw.auth.domain.RefreshToken;
import dev.tripdraw.auth.domain.RefreshTokenRepository;
import dev.tripdraw.auth.dto.OauthResponse;
import dev.tripdraw.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TokenGenerateService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public OauthResponse refresh(String token) {
        jwtTokenProvider.validateRefreshToken(token);
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException(INVALID_TOKEN));
        return generate(refreshToken.memberId());
    }

    public OauthResponse generate(Long memberId) {
        String accessToken = jwtTokenProvider.generateAccessToken(memberId.toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        refreshTokenRepository.deleteByMemberId(memberId);
        refreshTokenRepository.save(new RefreshToken(memberId, refreshToken));
        return new OauthResponse(accessToken, refreshToken);
    }

    public OauthResponse generateEmptyToken() {
        return new OauthResponse("", "");
    }
}
