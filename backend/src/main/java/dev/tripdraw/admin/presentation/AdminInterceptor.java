package dev.tripdraw.admin.presentation;

import static dev.tripdraw.admin.exception.AdminExceptionType.ADMIN_AUTH_FAIL;

import dev.tripdraw.admin.application.AdminAuthService;
import dev.tripdraw.admin.exception.AdminException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class AdminInterceptor implements HandlerInterceptor {

    private static final String SESSION = "SESSION";

    private final AdminAuthService adminAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new AdminException(ADMIN_AUTH_FAIL);
        }
        String uuid = (String) session.getAttribute(SESSION);
        adminAuthService.validateSession(uuid);
        return true;
    }
}
