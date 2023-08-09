package dev.tripdraw.dto.auth;

import dev.tripdraw.domain.oauth.OauthType;

public record OauthRequest(OauthType oauthType, String oauthToken) {
}
