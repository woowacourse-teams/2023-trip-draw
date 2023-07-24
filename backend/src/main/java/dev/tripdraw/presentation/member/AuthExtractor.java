package dev.tripdraw.presentation.member;

import static dev.tripdraw.exception.auth.AuthExceptionType.NO_AUTH_HEADER;
import static org.springframework.util.StringUtils.hasText;

import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.exception.auth.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthExtractor {

    private final Base64Decoder base64Decoder;

    public AuthExtractor(Base64Decoder base64Decoder) {
        this.base64Decoder = base64Decoder;
    }

    public LoginUser extract(HttpServletRequest request) {
        String encodedNickname = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!hasText(encodedNickname)) {
            throw new AuthException(NO_AUTH_HEADER);
        }

        String nickname = base64Decoder.decode(encodedNickname);

        return new LoginUser(nickname);
    }
}
