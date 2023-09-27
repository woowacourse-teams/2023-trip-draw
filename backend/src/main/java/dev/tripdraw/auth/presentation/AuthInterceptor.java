package dev.tripdraw.auth.presentation;

import static dev.tripdraw.auth.exception.AuthExceptionType.AUTH_FAIL;

import dev.tripdraw.auth.application.AuthExtractor;
import dev.tripdraw.auth.exception.AuthException;
import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.member.application.MemberService;
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
