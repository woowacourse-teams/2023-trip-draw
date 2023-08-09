package dev.tripdraw.presentation.member;

import static dev.tripdraw.exception.auth.AuthExceptionType.INVALID_AUTH_HEADER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.StringUtils.hasText;

import dev.tripdraw.application.oauth.AuthTokenManager;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.exception.auth.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthExtractor {

    private static final String KEYWORD = "Bearer ";
    private static final String EMPTY = "";

    private final AuthTokenManager authTokenManager;

    public AuthExtractor(AuthTokenManager authTokenManager) {
        this.authTokenManager = authTokenManager;
    }

    public LoginUser extract(HttpServletRequest request) {
        Long memberId = parse(request);
        return new LoginUser(memberId);
    }

    private Long parse(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        validate(header);
        return parseCredential(header);
    }

    private static void validate(String header) {
        if (!hasText(header) || !header.startsWith(KEYWORD)) {
            throw new AuthException(INVALID_AUTH_HEADER);
        }
    }

    private Long parseCredential(String header) {
        String credential = header.replace(KEYWORD, EMPTY);
        return authTokenManager.extractMemberId(credential);
    }
}
