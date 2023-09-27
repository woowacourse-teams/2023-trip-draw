package dev.tripdraw.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenRefreshRequest(

        @Schema(description = "Refresh Token", example = TOKEN_SAMPLE)
        String refreshToken
) {
    private static final String TOKEN_SAMPLE = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzMiLCJleHAiOjE2OTE4OTA0NjZ9.NlzZEEDWdjovafRPCD1ZvddeRUccZfyZpcUWzGPAI4oK-PKyPM64TIMJ3HyOy29vJtg_MET1c4omWUkGOb4qyQ";
}
