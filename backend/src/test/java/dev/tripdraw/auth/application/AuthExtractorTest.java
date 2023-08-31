package dev.tripdraw.auth.application;

import static dev.tripdraw.auth.exception.AuthExceptionType.INVALID_AUTH_HEADER;
import static dev.tripdraw.auth.exception.AuthExceptionType.INVALID_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dev.tripdraw.auth.dto.LoginUser;
import dev.tripdraw.auth.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthExtractorTest {

    private static final String TEST_SECRET_KEY = "ItIsALocalKeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee2222222222222222222222222222222222eeeeeeeeeeyXD";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;

    private AuthTokenManager authTokenManager;

    @BeforeEach
    void setUp() {
        authTokenManager = new AuthTokenManager(new JwtTokenProvider(TEST_SECRET_KEY), ACCESS_TOKEN_EXPIRE_TIME);
    }

    @Test
    void 요청_헤더에서_LoginUser를_추출한다() {
        // given
        AuthExtractor authExtractor = new AuthExtractor(authTokenManager);
        String accessToken = authTokenManager.generate(1L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        String encoded = "Bearer " + accessToken;
        when(request.getHeader(AUTHORIZATION)).thenReturn(encoded);

        // when
        LoginUser loginUser = authExtractor.extract(request);

        // then
        assertThat(loginUser.memberId()).isEqualTo(1L);
    }

    @Test
    void 요청_헤더에_Bearer_형식이_아닌_다른_인증정보를_사용하는_경우_예외가_발생한다() {
        // given
        AuthExtractor authExtractor = new AuthExtractor(
                new AuthTokenManager(new JwtTokenProvider(TEST_SECRET_KEY), ACCESS_TOKEN_EXPIRE_TIME)
        );
        HttpServletRequest request = mock(HttpServletRequest.class);
        String encoded = "Basic eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjkxNTE1Mzk2fQ.WEDBjEXfIAd4MaUTK29ElnnGYmYNCKSHLGVPJHlyf2DNECDX8QDqvigUCBzO4ULmpnxr4GiZZqdQyeH1BgU0Ag";
        when(request.getHeader(AUTHORIZATION)).thenReturn(encoded);

        // expect
        assertThatThrownBy(() -> authExtractor.extract(request))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_AUTH_HEADER.message());
    }

    @Test
    void 요청_헤더에_인증_정보가_없을_경우_예외를_발생시킨다() {
        // given
        AuthExtractor authExtractor = new AuthExtractor(
                new AuthTokenManager(new JwtTokenProvider(TEST_SECRET_KEY), ACCESS_TOKEN_EXPIRE_TIME)
        );
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AUTHORIZATION)).thenReturn(null);

        // expect
        assertThatThrownBy(() -> authExtractor.extract(request))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_AUTH_HEADER.message());
    }

    @Test
    void 헤더의_담긴_토큰이_유효하지_않으면_예외를_발생시킨다() {
        // given
        AuthExtractor authExtractor = new AuthExtractor(authTokenManager);
        HttpServletRequest request = mock(HttpServletRequest.class);
        String notEncoded = "Bearer wrong.long.token";
        when(request.getHeader(AUTHORIZATION)).thenReturn(notEncoded);

        // expect
        Assertions.assertThatThrownBy(() -> authExtractor.extract(request))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_TOKEN.message());
    }
}
