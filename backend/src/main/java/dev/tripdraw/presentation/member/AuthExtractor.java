package dev.tripdraw.presentation.member;

import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.exception.BadRequestException;
import dev.tripdraw.exception.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthExtractor {

    private final BasicAuthorizationDecoder basicAuthorizationDecoder;

    public LoginUser extract(HttpServletRequest request) {

        String authorization = request.getHeader(HttpServletRequest.BASIC_AUTH);
        if (authorization == null) {
            throw new BadRequestException(ExceptionCode.NO_AUTH_HEADER);
        }

        String nickname = basicAuthorizationDecoder.decode(authorization);

        return new LoginUser(nickname);
    }
}
