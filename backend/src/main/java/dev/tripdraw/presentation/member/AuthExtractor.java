package dev.tripdraw.presentation.member;

import static dev.tripdraw.exception.auth.AuthExceptionType.NO_AUTH_HEADER;
import static jakarta.servlet.http.HttpServletRequest.BASIC_AUTH;
import static org.springframework.util.StringUtils.hasText;

import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.exception.auth.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthExtractor {

    private final BasicAuthorizationDecoder basicAuthorizationDecoder;

    public AuthExtractor(BasicAuthorizationDecoder basicAuthorizationDecoder) {
        this.basicAuthorizationDecoder = basicAuthorizationDecoder;
    }

    public LoginUser extract(HttpServletRequest request) {
        String authorization = request.getHeader(BASIC_AUTH);
        if (!hasText(authorization)) {
            throw new AuthException(NO_AUTH_HEADER);
        }

        String nickname = basicAuthorizationDecoder.decode(authorization);

        return new LoginUser(nickname);
    }
}
