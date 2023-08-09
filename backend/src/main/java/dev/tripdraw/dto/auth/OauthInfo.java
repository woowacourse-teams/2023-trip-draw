package dev.tripdraw.dto.auth;

import dev.tripdraw.domain.oauth.OauthType;

public record OauthInfo(String oauthId, OauthType oauthType) {
}
