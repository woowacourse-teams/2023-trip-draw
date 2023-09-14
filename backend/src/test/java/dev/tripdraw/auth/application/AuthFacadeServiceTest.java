package dev.tripdraw.auth.application;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

import dev.tripdraw.auth.domain.RefreshToken;
import dev.tripdraw.auth.domain.RefreshTokenRepository;
import dev.tripdraw.auth.dto.OauthRequest;
import dev.tripdraw.auth.dto.OauthResponse;
import dev.tripdraw.auth.dto.RegisterRequest;
import dev.tripdraw.auth.dto.TokenRefreshRequest;
import dev.tripdraw.auth.oauth.OauthClientProvider;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.test.ServiceTest;
import dev.tripdraw.test.TestKakaoApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@ServiceTest
class AuthFacadeServiceTest {

    @Autowired
    private AuthFacadeService authFacadeService;

    @MockBean
    private OauthClientProvider oauthClientProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        when(oauthClientProvider.provide(KAKAO)).thenReturn(new TestKakaoApiClient());
    }

    @Test
    void 가입된_회원이_카카오_소셜_로그인하면_토큰이_포함된_응답을_반환한다() {
        // given
        memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        OauthRequest oauthRequest = new OauthRequest(KAKAO, "oauth.kakao.token");

        // when
        OauthResponse response = authFacadeService.login(oauthRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.accessToken()).isNotEmpty();
            softly.assertThat(response.refreshToken()).isNotEmpty();
        });
    }

    @Test
    void 신규_회원이_로그인하면_회원을_저장하고_빈_토큰이_포함된_응답을_반환한다() {
        // given
        OauthRequest oauthRequest = new OauthRequest(KAKAO, "oauth.kakao.token");

        // when
        OauthResponse response = authFacadeService.login(oauthRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.accessToken()).isEmpty();
            softly.assertThat(response.refreshToken()).isEmpty();
        });
    }

    @Test
    void 신규_회원의_닉네임을_등록하면_토큰이_포함된_응답을_반환한다() {
        // given
        Member member = Member.of("kakaoId", KAKAO);
        memberRepository.save(member);

        RegisterRequest registerRequest = new RegisterRequest("통후추", KAKAO, "oauth.kakao.token");

        // when
        OauthResponse response = authFacadeService.register(registerRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.accessToken()).isNotEmpty();
            softly.assertThat(response.refreshToken()).isNotEmpty();
        });
    }

    @Test
    void Refresh_토큰을_입력받아_Access_토큰과_Refresh_토큰을_재발급한다() {
        // given
        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        refreshTokenRepository.save(new RefreshToken(member.id(), refreshToken));
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(refreshToken);

        // when
        OauthResponse response = authFacadeService.refresh(tokenRefreshRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.accessToken()).isNotEmpty();
            softly.assertThat(response.refreshToken()).isNotEmpty();
            softly.assertThat(refreshTokenRepository.findByToken(refreshToken)).isEmpty();
            softly.assertThat(refreshTokenRepository.findByToken(response.refreshToken())).isPresent();
        });
    }
}
