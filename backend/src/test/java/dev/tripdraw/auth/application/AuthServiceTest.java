package dev.tripdraw.auth.application;

import static dev.tripdraw.auth.exception.AuthExceptionType.EXPIRED_REFRESH_TOKEN;
import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.member.exception.MemberExceptionType.DUPLICATE_NICKNAME;
import static dev.tripdraw.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.test.fixture.AuthFixture.만료된_토큰_생성용_ACCESS_TOKEN_설정;
import static dev.tripdraw.test.fixture.AuthFixture.만료된_토큰_생성용_REFRESH_TOKEN_설정;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

import dev.tripdraw.auth.domain.RefreshToken;
import dev.tripdraw.auth.domain.RefreshTokenRepository;
import dev.tripdraw.auth.dto.OauthRequest;
import dev.tripdraw.auth.dto.OauthResponse;
import dev.tripdraw.auth.dto.RegisterRequest;
import dev.tripdraw.auth.dto.TokenRefreshRequest;
import dev.tripdraw.auth.exception.AuthException;
import dev.tripdraw.auth.oauth.OauthClientProvider;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.member.exception.MemberException;
import dev.tripdraw.test.ServiceTest;
import dev.tripdraw.test.TestKakaoApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@ServiceTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

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
        OauthResponse response = authService.login(oauthRequest);

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
        OauthResponse response = authService.login(oauthRequest);

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
        OauthResponse response = authService.register(registerRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.accessToken()).isNotEmpty();
            softly.assertThat(response.refreshToken()).isNotEmpty();
        });
    }

    @Test
    void 신규_회원의_닉네임을_등록할_때_회원이_존재하지_않으면_예외가_발생한다() {
        // given
        RegisterRequest registerRequest = new RegisterRequest("저장안된후추", KAKAO, "oauth.kakao.token");

        // expect
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void 신규_회원의_닉네임을_등록할_때_이미_존재하는_닉네임이면_예외가_발생한다() {
        // given
        memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        RegisterRequest registerRequest = new RegisterRequest("통후추", KAKAO, "oauth.kakao.token");

        // expect
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(MemberException.class)
                .hasMessage(DUPLICATE_NICKNAME.message());
    }

    @Test
    void Refresh_토큰_재발급시_입력받은_토큰이_만료되는_경우_예외가_발생한다() {
        // given
        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        JwtTokenProvider expiredTokenProvider = new JwtTokenProvider(
                만료된_토큰_생성용_ACCESS_TOKEN_설정(),
                만료된_토큰_생성용_REFRESH_TOKEN_설정()
        );
        String expiredToken = expiredTokenProvider.generateRefreshToken();
        refreshTokenRepository.save(new RefreshToken(member.id(), expiredToken));
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(expiredToken);

        // expect
        assertThatThrownBy(() -> authService.refresh(tokenRefreshRequest))
                .isInstanceOf(AuthException.class)
                .hasMessage(EXPIRED_REFRESH_TOKEN.message());
    }

    @Test
    void Refresh_토큰을_입력받아_Access_토큰과_Refresh_토큰을_재발급한다() {
        // given
        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        refreshTokenRepository.save(new RefreshToken(member.id(), refreshToken));
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(refreshToken);

        // when
        OauthResponse response = authService.refresh(tokenRefreshRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.accessToken()).isNotEmpty();
            softly.assertThat(response.refreshToken()).isNotEmpty();
            softly.assertThat(refreshTokenRepository.findByToken(refreshToken)).isEmpty();
            softly.assertThat(refreshTokenRepository.findByToken(response.refreshToken())).isPresent();
        });
    }
}
