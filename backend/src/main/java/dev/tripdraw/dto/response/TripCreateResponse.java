package dev.tripdraw.dto.response;

import dev.tripdraw.domain.trip.Trip;

public record TripCreateResponse(Long tripId) {

    public static TripCreateResponse from(Trip savedTrip) {
        return new TripCreateResponse(savedTrip.getId());
    }
}
