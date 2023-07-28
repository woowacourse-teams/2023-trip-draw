package dev.tripdraw.dto.trip;

import io.swagger.v3.oas.annotations.media.Schema;

public record PointDeleteRequest(
        @Schema(description = "여행 Id", example = "1")
        Long tripId,

        @Schema(description = "위치 점 Id", example = "1")
        Long pointId
) {
}
