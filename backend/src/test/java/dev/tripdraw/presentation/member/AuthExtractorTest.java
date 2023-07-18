package dev.tripdraw.presentation.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthExtractorTest {

    @Test
    void 요청의_헤더에서_LoginUser를_추출한다() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader(HttpServletRequest.BASIC_AUTH)).thenReturn("Basic 7Ya17ZuE7LaU");

        LoginUser loginUser = AuthExtractor.extract(request);

        assertThat(loginUser.nickname()).isEqualTo("통후추");
    }

    @Test
    void 요청의_헤더에_인증_정보가_없을_경우_예외를_발생시킨다() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader(HttpServletRequest.BASIC_AUTH)).thenReturn(null);

        assertThatThrownBy(() -> AuthExtractor.extract(request)).isInstanceOf(BadRequestException.class);
    }
}
