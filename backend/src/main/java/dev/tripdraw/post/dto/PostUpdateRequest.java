package dev.tripdraw.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUpdateRequest(
        @Schema(description = "제목", example = "우도의 바닷가")
        @NotBlank @Size(max = 100)
        String title,

        @Schema(description = "내용", example = "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.")
        @Size(max = 10_000)
        String writing
) {
}
