package dev.tripdraw.application;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static dev.tripdraw.exception.auth.AuthExceptionType.EXPIRED_TOKEN;
import static dev.tripdraw.exception.auth.AuthExceptionType.INVALID_TOKEN;
import static dev.tripdraw.exception.member.MemberExceptionType.DUPLICATE_NICKNAME;
import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import dev.tripdraw.application.oauth.OauthClientProvider;
import dev.tripdraw.aspect.TimeTravelToFutureWhenRefreshAspect;
import dev.tripdraw.aspect.TimeTravelToPastWhenLogInAspect;
import dev.tripdraw.common.TestKakaoApiClient;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.dto.auth.AccessTokenRefreshRequest;
import dev.tripdraw.dto.auth.OauthRequest;
import dev.tripdraw.dto.auth.OauthResponse;
import dev.tripdraw.dto.auth.RegisterRequest;
import dev.tripdraw.exception.auth.AuthException;
import dev.tripdraw.exception.member.MemberException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "dev.tripdraw.aspect")
@ServiceTest
class AuthServiceTest {

    @MockBean
    OauthClientProvider oauthClientProvider;

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TimeTravelToPastWhenLogInAspect timeTravelToPastWhenLogInAspect;

    @Autowired
    private TimeTravelToFutureWhenRefreshAspect timeTravelToFutureWhenRefreshAspect;

    @BeforeEach
    void setUp() {
        when(oauthClientProvider.provide(KAKAO)).thenReturn(new TestKakaoApiClient());
    }

    @AfterEach
    void clear() {
        timeTravelToPastWhenLogInAspect.setCustomTime(null);
        timeTravelToFutureWhenRefreshAspect.setCustomTime(null);
    }

    @Test
    void 가입된_회원이_카카오_소셜_로그인하면_토큰이_포함된_응답을_반환한다() {
        // given
        memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        OauthRequest oauthRequest = new OauthRequest(KAKAO, "oauth.kakao.token");

        // when
        OauthResponse response = authService.login(oauthRequest);

        // then
        assertThat(response.accessToken()).isNotEmpty();
    }

    @Test
    void 신규_회원이_로그인하면_회원을_저장하고_빈_토큰이_포함된_응답을_반환한다() {
        // given
        OauthRequest oauthRequest = new OauthRequest(KAKAO, "oauth.kakao.token");

        // when
        OauthResponse response = authService.login(oauthRequest);

        // then
        assertThat(response.accessToken()).isEmpty();
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
        assertThat(response.accessToken()).isNotEmpty();
    }

    @Test
    void 신규_회원의_닉네임을_등록할_때_회원이_존재하지_않으면_예외가_발생한다() {
        // given
        RegisterRequest registerRequest = new RegisterRequest("저장안된후추", KAKAO, "oauth.kakao.token");

        // expect
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 신규_회원의_닉네임을_등록할_때_이미_존재하는_닉네임이면_예외가_발생한다() {
        // given
        memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        RegisterRequest registerRequest = new RegisterRequest("통후추", KAKAO, "oauth.kakao.token");

        // expect
        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(MemberException.class)
                .hasMessage(DUPLICATE_NICKNAME.getMessage());
    }

    @Test
    void 토큰이_유효하면_액세스_토큰을_갱신한다() {
        // given
        memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        OauthResponse oauthResponse = authService.login(new OauthRequest(KAKAO, "oauth.kakao.token"));
        String validToken = oauthResponse.accessToken();
        AccessTokenRefreshRequest accessTokenRefreshRequest = new AccessTokenRefreshRequest(validToken);

        // when
        timeTravelToFutureWhenRefreshAspect.setCustomTime("A few seconds later...");
        OauthResponse oauthResponseWithNewAccessToken = authService.refresh(accessTokenRefreshRequest);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(oauthResponseWithNewAccessToken.accessToken()).isNotEmpty();
            softly.assertThat(validToken).isNotEqualTo(oauthResponseWithNewAccessToken.accessToken());
        });
    }

    @Test
    void 액세스_토큰_갱신_시에_유효하지_않은_토큰이면_예외를_반환한다() {
        // given
        AccessTokenRefreshRequest refreshRequestWithInvalidToken = new AccessTokenRefreshRequest(
                "It.is.InvalidToken");

        // expect
        assertThatThrownBy(() -> authService.refresh(refreshRequestWithInvalidToken))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_TOKEN.getMessage());
    }

    @Test
    void 액세스_토큰_갱신_시에_만료된_토큰이면_예외를_반환한다() {
        // given
        timeTravelToPastWhenLogInAspect.setCustomTime("호랑이 담배 피던 시절...");
        memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        OauthResponse oauthResponse = authService.login(new OauthRequest(KAKAO, "oauth.kakao.token"));
        String expiredToken = oauthResponse.accessToken();
        AccessTokenRefreshRequest expiredAccessTokenRefreshRequest = new AccessTokenRefreshRequest(expiredToken);

        // expect
        assertThatThrownBy(() -> authService.refresh(expiredAccessTokenRefreshRequest))
                .isInstanceOf(AuthException.class)
                .hasMessage(EXPIRED_TOKEN.getMessage());
    }
}
