package dev.tripdraw.application;

import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.exception.member.MemberExceptionType.NICKNAME_CONFLICT;

import dev.tripdraw.application.oauth.AuthTokenManager;
import dev.tripdraw.application.oauth.OauthClient;
import dev.tripdraw.application.oauth.OauthClientProvider;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.dto.auth.OauthInfo;
import dev.tripdraw.dto.auth.OauthRequest;
import dev.tripdraw.dto.auth.OauthResponse;
import dev.tripdraw.dto.auth.RegisterRequest;
import dev.tripdraw.exception.member.MemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AuthService {

    private static final String EMPTY_TOKEN = "";

    private final MemberRepository memberRepository;
    private final OauthClientProvider oauthClientProvider;
    private final AuthTokenManager authTokenManager;

    public AuthService(
            MemberRepository memberRepository,
            OauthClientProvider oauthClientProvider,
            AuthTokenManager authTokenManager
    ) {
        this.memberRepository = memberRepository;
        this.oauthClientProvider = oauthClientProvider;
        this.authTokenManager = authTokenManager;
    }

    public OauthResponse login(OauthRequest oauthRequest) {
        OauthClient oauthClient = oauthClientProvider.provide(oauthRequest.oauthType());
        OauthInfo oauthInfo = oauthClient.requestOauthInfo(oauthRequest.oauthToken());

        String accessToken = memberRepository.findByOauthIdAndOauthType(oauthInfo.oauthId(), oauthInfo.oauthType())
                .map(member -> authTokenManager.generate(member.id()))
                .orElse(EMPTY_TOKEN);

        if (accessToken.isEmpty()) {
            memberRepository.save(Member.of(oauthInfo.oauthId(), oauthInfo.oauthType()));
        }

        return new OauthResponse(accessToken);
    }

    public OauthResponse register(RegisterRequest registerRequest) {
        OauthClient oauthClient = oauthClientProvider.provide(registerRequest.oauthType());
        OauthInfo oauthInfo = oauthClient.requestOauthInfo(registerRequest.oauthToken());

        Member member = memberRepository.findByOauthIdAndOauthType(oauthInfo.oauthId(), oauthInfo.oauthType())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        String nickname = registerRequest.nickname();
        validateNicknameDuplication(nickname);
        member.changeNickname(nickname);

        String accessToken = authTokenManager.generate(member.id());
        return new OauthResponse(accessToken);
    }

    private void validateNicknameDuplication(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new MemberException(NICKNAME_CONFLICT);
        }
    }
}
