package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Trip;
import io.swagger.v3.oas.annotations.media.Schema;

public record TripCreateResponse(
        @Schema(description = "여행 Id", example = "1")
        Long tripId
) {

    public static TripCreateResponse from(Trip trip) {
        return new TripCreateResponse(trip.id());
    }
}
