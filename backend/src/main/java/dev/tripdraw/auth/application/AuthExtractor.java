package dev.tripdraw.auth.application;

import static dev.tripdraw.auth.exception.AuthExceptionType.INVALID_AUTH_HEADER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.StringUtils.hasText;

import dev.tripdraw.auth.dto.LoginUser;
import dev.tripdraw.auth.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthExtractor {

    private static final String AUTHORIZATION_PREFIX = "Bearer ";
    private static final String EMPTY = "";

    private final AuthTokenManager authTokenManager;

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
        if (!hasText(header) || !header.startsWith(AUTHORIZATION_PREFIX)) {
            throw new AuthException(INVALID_AUTH_HEADER);
        }
    }

    private Long parseCredential(String header) {
        String credential = header.replace(AUTHORIZATION_PREFIX, EMPTY);
        return authTokenManager.extractMemberId(credential);
    }
}
