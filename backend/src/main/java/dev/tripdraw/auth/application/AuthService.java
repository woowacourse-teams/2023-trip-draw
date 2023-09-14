package dev.tripdraw.auth.application;

import static dev.tripdraw.auth.exception.AuthExceptionType.INVALID_TOKEN;
import static dev.tripdraw.member.exception.MemberExceptionType.DUPLICATE_NICKNAME;
import static dev.tripdraw.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;

import dev.tripdraw.auth.domain.RefreshToken;
import dev.tripdraw.auth.domain.RefreshTokenRepository;
import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.auth.dto.OauthResponse;
import dev.tripdraw.auth.exception.AuthException;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private static final OauthResponse EMPTY_TOKEN_RESPONSE = new OauthResponse("", "");

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    public OauthResponse login(OauthInfo oauthInfo) {
        return memberRepository.findByOauthIdAndOauthType(oauthInfo.oauthId(), oauthInfo.oauthType())
                .map(member -> generateOAuthResponse(member.id()))
                .orElseGet(() -> {
                    memberRepository.save(Member.of(oauthInfo.oauthId(), oauthInfo.oauthType()));
                    return EMPTY_TOKEN_RESPONSE;
                });
    }

    private OauthResponse generateOAuthResponse(Long memberId) {
        String accessToken = jwtTokenProvider.generateAccessToken(memberId.toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        refreshTokenRepository.deleteByMemberId(memberId);
        refreshTokenRepository.save(new RefreshToken(memberId, refreshToken));
        return new OauthResponse(accessToken, refreshToken);
    }

    public OauthResponse register(OauthInfo oauthInfo, String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new MemberException(DUPLICATE_NICKNAME);
        }

        Member member = memberRepository.findByOauthIdAndOauthType(oauthInfo.oauthId(), oauthInfo.oauthType())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        member.changeNickname(nickname);
        return generateOAuthResponse(member.id());
    }

    public OauthResponse refresh(String token) {
        jwtTokenProvider.validateRefreshToken(token);
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthException(INVALID_TOKEN));
        return generateOAuthResponse(refreshToken.memberId());
    }
}
