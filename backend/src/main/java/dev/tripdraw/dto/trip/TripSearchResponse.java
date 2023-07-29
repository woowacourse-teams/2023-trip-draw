package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Trip;
import io.swagger.v3.oas.annotations.media.Schema;

public record TripSearchResponse(
        @Schema(description = "여행 Id", example = "1")
        Long tripId,

        @Schema(description = "여행명", example = "통후추의 여행")
        String name
) {

    public static TripSearchResponse from(Trip trip) {
        return new TripSearchResponse(trip.id(), trip.nameValue());
    }
}
