package dev.tripdraw.trip.dto;

import dev.tripdraw.trip.domain.Trip;
import java.time.LocalDateTime;

public record TripSearchResponse(
        Long tripId,
        String name,
        String imageUrl,
        String routeImageUrl,
        LocalDateTime startTime,
        LocalDateTime endTime
) {

    public static TripSearchResponse from(Trip trip) {
        return new TripSearchResponse(
                trip.id(),
                trip.nameValue(),
                trip.imageUrl(),
                trip.routeImageUrl(),
                trip.createdAt(),
                trip.updatedAt()
        );
    }
}
