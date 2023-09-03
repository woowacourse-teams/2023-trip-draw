package dev.tripdraw.auth.dto;

import dev.tripdraw.common.auth.OauthType;

public record OauthInfo(String oauthId, OauthType oauthType) {
}
