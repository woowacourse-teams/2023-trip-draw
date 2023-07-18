package dev.tripdraw.presentation.member;

import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.exception.BadRequestException;
import dev.tripdraw.exception.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;

public class AuthExtractor {

    public static LoginUser extract(HttpServletRequest request) {

        String authorization = request.getHeader(HttpServletRequest.BASIC_AUTH);
        if (authorization == null) {
            throw new BadRequestException(ExceptionCode.NO_AUTH_HEADER);
        }

        String nickname = BasicAuthorizationDecoder.decode(authorization);

        return new LoginUser(nickname);
    }
}
