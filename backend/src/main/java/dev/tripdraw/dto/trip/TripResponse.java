package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Trip;
import java.util.List;

public record TripResponse(Long tripId, String name, List<PointResponse> routes) {

    public static TripResponse from(Trip trip) {
        return new TripResponse(trip.id(), trip.nameValue(), generateRoutes(trip));
    }

    private static List<PointResponse> generateRoutes(Trip trip) {
        return trip.points().stream()
                .map(PointResponse::from)
                .toList();
    }
}
