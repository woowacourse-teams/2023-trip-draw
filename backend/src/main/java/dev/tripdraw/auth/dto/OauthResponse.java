package dev.tripdraw.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record OauthResponse(
        @Schema(description = "Access Token", example = TOKEN_SAMPLE)

        String accessToken
) {
    private static final String TOKEN_SAMPLE = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzMiLCJleHAiOjE2OTE4OTA0NjZ9.NlzZEEDWdjovafRPCD1ZvddeRUccZfyZpcUWzGPAI4oK-PKyPM64TIMJ3HyOy29vJtg_MET1c4omWUkGOb4qyQ";
}
