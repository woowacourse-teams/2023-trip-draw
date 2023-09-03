package dev.tripdraw.auth.application;

import static dev.tripdraw.member.exception.MemberExceptionType.DUPLICATE_NICKNAME;
import static dev.tripdraw.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;

import dev.tripdraw.auth.domain.RefreshToken;
import dev.tripdraw.auth.domain.RefreshTokenRepository;
import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.auth.dto.OauthRequest;
import dev.tripdraw.auth.dto.OauthResponse;
import dev.tripdraw.auth.dto.RegisterRequest;
import dev.tripdraw.auth.oauth.OauthClient;
import dev.tripdraw.auth.oauth.OauthClientProvider;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.member.exception.MemberException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private static final String EMPTY_TOKEN = "";

    private final MemberRepository memberRepository;
    private final OauthClientProvider oauthClientProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public OauthResponse login(OauthRequest oauthRequest) {
        OauthClient oauthClient = oauthClientProvider.provide(oauthRequest.oauthType());
        OauthInfo oauthInfo = oauthClient.requestOauthInfo(oauthRequest.oauthToken());

        Optional<Member> member = memberRepository.findByOauthIdAndOauthType(
                oauthInfo.oauthId(),
                oauthInfo.oauthType()
        );
        if (member.isEmpty()) {
            memberRepository.save(Member.of(oauthInfo.oauthId(), oauthInfo.oauthType()));
            return new OauthResponse(EMPTY_TOKEN, EMPTY_TOKEN);
        }

        Member findMember = member.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        return generateToken(findMember);
    }

    private OauthResponse generateToken(Member member) {
        String accessToken = jwtTokenProvider.generateAccessToken(member.id().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        refreshTokenRepository.deleteByMemberId(member.id());
        RefreshToken savedRefreshToken = refreshTokenRepository.save(new RefreshToken(member.id(), refreshToken));
        return new OauthResponse(accessToken, savedRefreshToken.token());
    }

    public OauthResponse register(RegisterRequest registerRequest) {
        OauthClient oauthClient = oauthClientProvider.provide(registerRequest.oauthType());
        OauthInfo oauthInfo = oauthClient.requestOauthInfo(registerRequest.oauthToken());

        Member member = memberRepository.findByOauthIdAndOauthType(oauthInfo.oauthId(), oauthInfo.oauthType())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        String nickname = registerRequest.nickname();
        validateDuplicateNickname(nickname);
        member.changeNickname(nickname);
        return generateToken(member);
    }

    private void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new MemberException(DUPLICATE_NICKNAME);
        }
    }
}
