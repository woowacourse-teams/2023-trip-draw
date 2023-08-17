package dev.tripdraw.presentation.member;

import static dev.tripdraw.exception.auth.AuthExceptionType.AUTH_FAIL;

import dev.tripdraw.application.MemberService;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.exception.auth.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final MemberService memberService;
    private final AuthExtractor authExtractor;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        LoginUser loginUser = authExtractor.extract(request);

        if (!memberService.existsById(loginUser.memberId())) {
            throw new AuthException(AUTH_FAIL);
        }

        return true;
    }
}
