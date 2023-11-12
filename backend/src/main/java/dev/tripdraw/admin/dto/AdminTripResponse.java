package dev.tripdraw.admin.dto;

import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripStatus;
import dev.tripdraw.trip.dto.PointResponse;
import java.util.List;
import java.util.Objects;

public record AdminTripResponse(
        Long tripId,
        String name,
        List<PointResponse> route,
        TripStatus status,
        String imageUrl,
        String routeImageUrl
) {

    private static final String EMPTY_IMAGE_URL = "";

    public static AdminTripResponse from(Trip trip) {
        return new AdminTripResponse(
                trip.id(),
                trip.nameValue(),
                generateRoute(trip),
                trip.status(),
                Objects.requireNonNullElse(trip.imageUrl(), EMPTY_IMAGE_URL),
                Objects.requireNonNullElse(trip.routeImageUrl(), EMPTY_IMAGE_URL)
        );
    }

    private static List<PointResponse> generateRoute(Trip trip) {
        return trip.points().stream()
                .map(PointResponse::from)
                .toList();
    }
}

