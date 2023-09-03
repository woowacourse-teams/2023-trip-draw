package dev.tripdraw.auth.application;

import static dev.tripdraw.auth.exception.AuthExceptionType.INVALID_AUTH_HEADER;
import static dev.tripdraw.auth.exception.AuthExceptionType.INVALID_TOKEN;
import static dev.tripdraw.test.fixture.AuthFixture.테스트_ACCESS_TOKEN_설정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import dev.tripdraw.auth.exception.AuthException;
import dev.tripdraw.common.auth.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthExtractorTest {

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(테스트_ACCESS_TOKEN_설정());
    private final AuthExtractor authExtractor = new AuthExtractor(jwtTokenProvider);

    @Test
    void 요청_헤더에서_LoginUser를_추출한다() {
        // given
        String accessToken = jwtTokenProvider.generateAccessToken("1");
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
        HttpServletRequest request = mock(HttpServletRequest.class);
        String encoded = "Basic aGVsbG86d29ybGQ=";
        when(request.getHeader(AUTHORIZATION)).thenReturn(encoded);

        // expect
        assertThatThrownBy(() -> authExtractor.extract(request))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_AUTH_HEADER.message());
    }

    @Test
    void 요청_헤더에_인증_정보가_없을_경우_예외를_발생시킨다() {
        // given
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
        HttpServletRequest request = mock(HttpServletRequest.class);
        String notEncoded = "Bearer wrong.long.token";
        when(request.getHeader(AUTHORIZATION)).thenReturn(notEncoded);

        // expect
        Assertions.assertThatThrownBy(() -> authExtractor.extract(request))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_TOKEN.message());
    }
}
