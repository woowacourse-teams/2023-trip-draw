package dev.tripdraw.presentation.member;

import static dev.tripdraw.exception.auth.AuthExceptionType.INVALID_AUTH_HEADER;
import static dev.tripdraw.exception.auth.AuthExceptionType.INVALID_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.tripdraw.application.oauth.AuthTokenManager;
import dev.tripdraw.application.oauth.JwtTokenProvider;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.exception.auth.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthExtractorTest {

    private AuthTokenManager authTokenManager;

    private static final String TEST_SECRET_KEY = "ItIsALocalKeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee2222222222222222222222222222222222eeeeeeeeeeyXD";

    @BeforeEach
    void setUp() {
        authTokenManager = new AuthTokenManager(new JwtTokenProvider(TEST_SECRET_KEY));
    }

    @Test
    void 요청_헤더에서_LoginUser를_추출한다() {
        // given
        AuthExtractor authExtractor = new AuthExtractor(authTokenManager);
        String accessToken = authTokenManager.generate(1L);
        HttpServletRequest request = mock(HttpServletRequest.class);
        String encoded = "Bearer " + accessToken;
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(encoded);

        // when
        LoginUser loginUser = authExtractor.extract(request);

        // then
        assertThat(loginUser.memberId()).isEqualTo(1L);
    }

    @Test
    void 요청_헤더에_Bearer_형식이_아닌_다른_인증정보를_사용하는_경우_예외가_발생한다() {
        // given
        AuthExtractor authExtractor = new AuthExtractor(
                new AuthTokenManager(new JwtTokenProvider(TEST_SECRET_KEY))
        );
        HttpServletRequest request = mock(HttpServletRequest.class);
        String encoded = "Basic eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjkxNTE1Mzk2fQ.WEDBjEXfIAd4MaUTK29ElnnGYmYNCKSHLGVPJHlyf2DNECDX8QDqvigUCBzO4ULmpnxr4GiZZqdQyeH1BgU0Ag";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(encoded);

        // expect
        assertThatThrownBy(() -> authExtractor.extract(request))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_AUTH_HEADER.getMessage());
    }

    @Test
    void 요청_헤더에_인증_정보가_없을_경우_예외를_발생시킨다() {
        // given
        AuthExtractor authExtractor = new AuthExtractor(
                new AuthTokenManager(new JwtTokenProvider(TEST_SECRET_KEY))
        );
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        // expect
        assertThatThrownBy(() -> authExtractor.extract(request))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_AUTH_HEADER.getMessage());
    }

    @Test
    void 헤더의_담긴_토큰이_유효하지_않으면_예외를_발생시킨다() {
        // given
        AuthExtractor authExtractor = new AuthExtractor(authTokenManager);
        HttpServletRequest request = mock(HttpServletRequest.class);
        String notEncoded = "Bearer wrong.long.token";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(notEncoded);

        // expect
        Assertions.assertThatThrownBy(() -> authExtractor.extract(request))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_TOKEN.getMessage());
    }
}
