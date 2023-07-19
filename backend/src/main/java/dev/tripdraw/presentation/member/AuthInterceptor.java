package dev.tripdraw.presentation.member;

import dev.tripdraw.application.MemberService;
import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.exception.auth.AuthException;
import dev.tripdraw.exception.auth.AuthExceptionType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

@Component
public class AuthInterceptor extends WebRequestHandlerInterceptorAdapter {

    private final MemberService memberService;
    private final AuthExtractor authExtractor;

    public AuthInterceptor(
            WebRequestInterceptor requestInterceptor,
            MemberService memberService,
            AuthExtractor authExtractor
    ) {
        super(requestInterceptor);
        this.memberService = memberService;
        this.authExtractor = authExtractor;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        LoginUser loginUser = authExtractor.extract(request);

        if (!memberService.existsByNickname(loginUser.nickname())) {
            throw new AuthException(AuthExceptionType.MEMBER_NOT_FOUND);
        }

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView
    ) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
