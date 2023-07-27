package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Status;
import dev.tripdraw.domain.trip.Trip;
import java.util.List;

public record TripResponse(Long tripId, String name, List<PointResponse> routes, Status status) {

    public static TripResponse from(Trip trip) {
        return new TripResponse(trip.id(), trip.nameValue(), generateRoutes(trip), trip.status());
    }

    private static List<PointResponse> generateRoutes(Trip trip) {
        return trip.points().stream()
                .map(PointResponse::from)
                .toList();
    }
}
