package dev.tripdraw.auth.dto;

import dev.tripdraw.auth.domain.OauthType;

public record OauthInfo(String oauthId, OauthType oauthType) {
}
