package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Trip;
import java.util.List;

public record TripCreateResponse(Long tripId, String name, List<PointCreateResponse> routes) {

    public static TripCreateResponse from(Trip trip) {
        return new TripCreateResponse(trip.id(), trip.nameValue(), generateRoutes(trip));
    }

    private static List<PointCreateResponse> generateRoutes(Trip trip) {
        return trip.points().stream()
                .map(PointCreateResponse::from)
                .toList();
    }
}
