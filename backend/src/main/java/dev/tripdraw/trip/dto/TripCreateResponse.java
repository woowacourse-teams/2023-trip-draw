package dev.tripdraw.trip.dto;

import dev.tripdraw.trip.domain.Trip;
import io.swagger.v3.oas.annotations.media.Schema;

public record TripCreateResponse(
        @Schema(description = "여행 Id", example = "1")
        Long tripId
) {

    public static TripCreateResponse from(Trip trip) {
        return new TripCreateResponse(trip.id());
    }
}
