package dev.tripdraw.area.application;

import java.util.Map;

public record OpenAPIAccessTokenResponse(
        String id,
        Map<String, String> result,
        String errMsg,
        String errCd,
        String trId
) {
}
