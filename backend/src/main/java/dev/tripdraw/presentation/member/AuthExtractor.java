package dev.tripdraw.presentation.member;

import static dev.tripdraw.exception.auth.AuthExceptionType.INVALID_AUTH_HEADER;
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

    private static final String KEYWORD = "Bearer ";
    private static final String EMPTY = "";

    private final Decoder decoder = Base64.getDecoder();

    public LoginUser extract(HttpServletRequest request) {
        String credential = parse(request);
        return new LoginUser(credential);
    }

    private String parse(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        validate(header);
        return parseCredential(header);
    }

    private static void validate(String header) {
        if (!hasText(header) || !header.startsWith(KEYWORD)) {
            throw new AuthException(INVALID_AUTH_HEADER);
        }
    }

    private String parseCredential(String header) {
        final String credential = header.replace(KEYWORD, EMPTY);
        return decodeBase64(credential);
    }

    private String decodeBase64(String credential) {
        try {
            return new String(decoder.decode(credential));
        } catch (IllegalArgumentException e) {
            throw new AuthException(INVALID_AUTH_HEADER);
        }
    }
}
