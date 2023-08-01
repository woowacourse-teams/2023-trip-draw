package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.TripStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record TripUpdateRequest(
        @Schema(description = "여행명", example = "통후추의 여행")
        String name,

        @Schema(description = "여행 상태 (option : ONGOING, FINISHED)", example = "ONGOING")
        TripStatus status
) {
}
