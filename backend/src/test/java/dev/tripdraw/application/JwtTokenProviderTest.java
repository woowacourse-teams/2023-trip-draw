package dev.tripdraw.application;

import static dev.tripdraw.exception.auth.AuthExceptionType.EXPIRED_TOKEN;
import static dev.tripdraw.exception.auth.AuthExceptionType.INVALID_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.application.oauth.JwtTokenProvider;
import dev.tripdraw.exception.auth.AuthException;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JwtTokenProviderTest {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;        //30분
    private static final Date EXPIRED_AT = new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRE_TIME);

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        String secretKey = "test2222222222eeeeeeeeeeee222222222asdfasdfasdfasdfasdssssssssssaaaaaaaaaavvvvvvvfsdfsf2eeeeeeeeeeeee";
        jwtTokenProvider = new JwtTokenProvider(secretKey);
    }

    @Test
    void 토큰_정보를_추출한다() {
        // given
        String accessToken = jwtTokenProvider.generate("memberId", EXPIRED_AT);

        // when
        String subject = jwtTokenProvider.extractSubject(accessToken);

        // then
        assertThat(subject).isEqualTo("memberId");
    }

    @Test
    void 대상과_만료기한을_입력_받아_토큰을_생성한다() {
        // given
        String subject = "memberId";

        // when
        String accessToken = jwtTokenProvider.generate(subject, EXPIRED_AT);

        // then
        assertThat(accessToken).isNotEmpty();
    }

    @Test
    void 유효하지_않은_토큰의_정보를_추출할_때_예외를_발생시킨다() {
        // given
        String invalidToken = "Invalid.Token.XD";

        // expect
        assertThatThrownBy(() -> jwtTokenProvider.extractSubject(invalidToken))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_TOKEN.getMessage());
    }

    @Test
    void 만료된_토큰의_정보를_추출할_때_예외를_발생시킨다() {
        // given
        Date expiredDate = Date.from(Instant.now().minusSeconds(1));
        String expiredToken = jwtTokenProvider.generate("memberId", expiredDate);

        // expect
        assertThatThrownBy(() -> jwtTokenProvider.extractSubject(expiredToken))
                .isInstanceOf(AuthException.class)
                .hasMessage(EXPIRED_TOKEN.getMessage());
    }
}
