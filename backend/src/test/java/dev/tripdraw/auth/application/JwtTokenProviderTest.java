package dev.tripdraw.auth.application;

import static dev.tripdraw.auth.exception.AuthExceptionType.EXPIRED_TOKEN;
import static dev.tripdraw.auth.exception.AuthExceptionType.INVALID_TOKEN;
import static dev.tripdraw.test.fixture.AuthFixture.만료된_토큰_생성용_ACCESS_TOKEN_설정;
import static dev.tripdraw.test.fixture.AuthFixture.테스트_ACCESS_TOKEN_설정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.auth.exception.AuthException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JwtTokenProviderTest {

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(테스트_ACCESS_TOKEN_설정());

    @Test
    void 토큰_정보를_추출한다() {
        // given
        String accessToken = jwtTokenProvider.generateAccessToken("memberId");

        // when
        String subject = jwtTokenProvider.extractAccessToken(accessToken);

        // then
        assertThat(subject).isEqualTo("memberId");
    }

    @Test
    void 대상과_만료기한을_입력_받아_토큰을_생성한다() {
        // given
        String subject = "memberId";

        // when
        String accessToken = jwtTokenProvider.generateAccessToken(subject);

        // then
        assertThat(accessToken).isNotEmpty();
    }

    @Test
    void 유효하지_않은_토큰의_정보를_추출할_때_예외를_발생시킨다() {
        // given
        String invalidToken = "Invalid.Token.XD";

        // expect
        assertThatThrownBy(() -> jwtTokenProvider.extractAccessToken(invalidToken))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_TOKEN.message());
    }

    @Test
    void 만료된_토큰의_정보를_추출할_때_예외를_발생시킨다() {
        // given
        JwtTokenProvider expiredTokenProvider = new JwtTokenProvider(만료된_토큰_생성용_ACCESS_TOKEN_설정());
        String expiredToken = expiredTokenProvider.generateAccessToken("memberId");

        // expect
        assertThatThrownBy(() -> jwtTokenProvider.extractAccessToken(expiredToken))
                .isInstanceOf(AuthException.class)
                .hasMessage(EXPIRED_TOKEN.message());
    }
}
