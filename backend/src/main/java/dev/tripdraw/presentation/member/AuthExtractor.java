package dev.tripdraw.presentation.member;

import static dev.tripdraw.exception.auth.AuthExceptionType.NOT_ENCODED_BY_BASE64;
import static dev.tripdraw.exception.auth.AuthExceptionType.NO_AUTH_HEADER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.StringUtils.hasText;

import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.exception.auth.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Base64.Decoder;
import org.springframework.stereotype.Component;

@Component
public class AuthExtractor {

    public LoginUser extract(HttpServletRequest request) {
        String encodedNickname = getEncodedNickname(request);
        return getLoginUser(encodedNickname);
    }

    private String getEncodedNickname(HttpServletRequest request) {
        String encodedNickname = request.getHeader(AUTHORIZATION);
        if (!hasText(encodedNickname)) {
            throw new AuthException(NO_AUTH_HEADER);
        }
        return encodedNickname;
    }

    private LoginUser getLoginUser(String encodedNickname) {
        Decoder decoder = Base64.getDecoder();
        try {
            String nickname = new String(decoder.decode(encodedNickname));
            return new LoginUser(nickname);
        } catch (IllegalArgumentException e) {
            throw new AuthException(NOT_ENCODED_BY_BASE64);
        }
    }
}
