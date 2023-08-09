package dev.tripdraw.dto.auth;

import dev.tripdraw.domain.oauth.OauthType;

public record RegisterRequest(String nickname, OauthType oauthType, String oauthToken) {
}
