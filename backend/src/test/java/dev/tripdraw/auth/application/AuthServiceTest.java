package dev.tripdraw.auth.application;

import static dev.tripdraw.auth.exception.AuthExceptionType.EXPIRED_REFRESH_TOKEN;
import static dev.tripdraw.auth.exception.AuthExceptionType.INVALID_TOKEN;
import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.test.fixture.AuthFixture.만료된_토큰_생성용_ACCESS_TOKEN_설정;
import static dev.tripdraw.test.fixture.AuthFixture.만료된_토큰_생성용_REFRESH_TOKEN_설정;
import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import dev.tripdraw.auth.domain.RefreshToken;
import dev.tripdraw.auth.domain.RefreshTokenRepository;
import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.auth.dto.OauthResponse;
import dev.tripdraw.auth.exception.AuthException;
import dev.tripdraw.common.auth.OauthType;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.member.exception.MemberException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void 신규_회원이_로그인하면_회원을_저장하고_빈_토큰이_포함된_응답을_반환한다() {
        // given
        given(memberRepository.findByOauthIdAndOauthType(any(String.class), any(OauthType.class)))
                .willReturn(Optional.empty());

        OauthInfo oauthInfo = new OauthInfo("id", KAKAO);

        // when
        OauthResponse response = authService.login(oauthInfo);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.accessToken()).isEmpty();
            softly.assertThat(response.refreshToken()).isEmpty();
        });
    }

    @Test
    void 신규_회원의_닉네임을_등록하면_토큰이_포함된_응답을_반환한다() {
        // given
        given(memberRepository.findByOauthIdAndOauthType(any(String.class), any(OauthType.class)))
                .willReturn(Optional.of(사용자()));

        OauthInfo oauthInfo = new OauthInfo("id", KAKAO);

        // when
        OauthResponse response = authService.register(oauthInfo, "통후추");

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.accessToken()).isNotEmpty();
            softly.assertThat(response.refreshToken()).isNotEmpty();
        });
    }

    @Test
    void 신규_회원의_닉네임을_등록할_때_회원이_존재하지_않으면_예외가_발생한다() {
        // given
        given(memberRepository.findByOauthIdAndOauthType(any(String.class), any(OauthType.class)))
                .willReturn(Optional.empty());

        OauthInfo unregisteredOauthInfo = new OauthInfo("id", KAKAO);

        // expect
        assertThatThrownBy(() -> authService.register(unregisteredOauthInfo, "통후추"))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void Refresh_토큰_재발급시_입력받은_Refresh_토큰이_만료된_경우_예외가_발생한다() {
        // given
        JwtTokenProvider expiredTokenProvider = new JwtTokenProvider(
                만료된_토큰_생성용_ACCESS_TOKEN_설정(),
                만료된_토큰_생성용_REFRESH_TOKEN_설정()
        );
        String expiredToken = expiredTokenProvider.generateRefreshToken();

        // expect
        assertThatThrownBy(() -> authService.refresh(expiredToken))
                .isInstanceOf(AuthException.class)
                .hasMessage(EXPIRED_REFRESH_TOKEN.message());
    }

    @Test
    void Refresh_토큰_재발급시_입력받은_Refresh_토큰이_존재하지_않는_토큰인_경우_예외가_발생한다() {
        // given
        given(refreshTokenRepository.findByToken(any(String.class))).willReturn(Optional.empty());
        String token = jwtTokenProvider.generateRefreshToken();

        // expect
        assertThatThrownBy(() -> authService.refresh(token))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_TOKEN.message());
    }

    @Test
    void Refresh_토큰을_입력받아_Access_토큰과_Refresh_토큰을_재발급한다() {
        // given
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        given(refreshTokenRepository.findByToken(any(String.class)))
                .willReturn(Optional.of(new RefreshToken(1L, 1L, refreshToken)));

        // when
        OauthResponse response = authService.refresh(refreshToken);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.accessToken()).isNotEmpty();
            softly.assertThat(response.refreshToken()).isNotEmpty();
            softly.assertThat(response.refreshToken()).isNotEqualTo(refreshToken);
        });
    }
}
