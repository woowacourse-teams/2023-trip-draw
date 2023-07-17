package dev.tripdraw.presentation.member;

import jakarta.servlet.http.HttpServletRequest;

public class AuthExtractor {

    public static LoginUser extract(HttpServletRequest request) {

        // TODO: 헤더 없는 경우 예외 처리 기능 구현
        String authorization = request.getHeader(HttpServletRequest.BASIC_AUTH);
        String nickname = BasicAuthorizationDecoder.decode(authorization);

        return new LoginUser(nickname);
    }
}
