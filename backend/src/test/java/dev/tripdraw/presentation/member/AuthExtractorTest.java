package dev.tripdraw.presentation.member;

import static dev.tripdraw.exception.auth.AuthExceptionType.INVALID_AUTH_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.exception.auth.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthExtractorTest {

    @Test
    void 요청_헤더에서_LoginUser를_추출한다() {
        // given
        AuthExtractor authExtractor = new AuthExtractor();
        HttpServletRequest request = mock(HttpServletRequest.class);
        String encoded = "Bearer 7Ya17ZuE7LaU";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(encoded);

        // when
        LoginUser loginUser = authExtractor.extract(request);

        // then
        assertThat(loginUser.nickname()).isEqualTo("통후추");
    }

    @Test
    void 요청_헤더에_Bearer_형식이_아닌_다른_인증정보를_사용하는_경우_예외가_발생한다() {
        // given
        AuthExtractor authExtractor = new AuthExtractor();
        HttpServletRequest request = mock(HttpServletRequest.class);
        String encoded = "Basic 7Ya17ZuE7LaU";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(encoded);

        // expect
        assertThatThrownBy(() -> authExtractor.extract(request))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_AUTH_HEADER.getMessage());
    }

    @Test
    void 요청_헤더에_인증_정보가_없을_경우_예외를_발생시킨다() {
        // given
        AuthExtractor authExtractor = new AuthExtractor();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        // expect
        assertThatThrownBy(() -> authExtractor.extract(request))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_AUTH_HEADER.getMessage());
    }

    @Test
    void 헤더의_내용이_base64로_암호화된_문자열이_아닌_경우_예외를_발생시킨다() {
        // given
        AuthExtractor authExtractor = new AuthExtractor();
        HttpServletRequest request = mock(HttpServletRequest.class);
        String notEncoded = "Bearer 통후추";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(notEncoded);

        // expect
        Assertions.assertThatThrownBy(() -> authExtractor.extract(request))
                .isInstanceOf(AuthException.class);
    }
}
