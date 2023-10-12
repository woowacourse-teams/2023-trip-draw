package dev.tripdraw.admin.presentation;

import static dev.tripdraw.admin.exception.AdminExceptionType.ADMIN_AUTH_FAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

import dev.tripdraw.admin.domain.AdminSession;
import dev.tripdraw.admin.domain.AdminSessionRepository;
import dev.tripdraw.admin.exception.AdminException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
class AdminInterceptorTest {

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Autowired
    private AdminSessionRepository adminSessionRepository;

    @Test
    void 세션이_null이라면_예외가_발생한다() {
        // given
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

        // expect
        assertThatThrownBy(() -> adminInterceptor.preHandle(httpServletRequest, httpServletResponse, null))
                .isInstanceOf(AdminException.class)
                .hasMessage(ADMIN_AUTH_FAIL.message());
    }

    @Test
    void 올바른_세션이라면_예외가_발생하지_않고_true를_반환한다() {
        // given
        AdminSession adminSession = adminSessionRepository.save(new AdminSession());
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        HttpSession httpSession = mock(HttpSession.class);
        given(httpServletRequest.getSession(false)).willReturn(httpSession);
        given(httpSession.getAttribute("SESSION")).willReturn(adminSession.uuid().toString());

        // expect
        assertThat(adminInterceptor.preHandle(httpServletRequest, httpServletResponse, null)).isTrue();
    }
}
