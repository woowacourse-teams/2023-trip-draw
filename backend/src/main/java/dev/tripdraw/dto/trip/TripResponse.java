package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Trip;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record TripResponse(
        @Schema(description = "여행 Id", example = "1")
        Long tripId,

        @Schema(description = "여행명", example = "통후추의 여행")
        String name,

        @Schema(description = "경로")
        List<PointResponse> routes
) {

    public static TripResponse from(Trip trip) {
        return new TripResponse(trip.id(), trip.nameValue(), generateRoutes(trip));
    }

    private static List<PointResponse> generateRoutes(Trip trip) {
        return trip.points().stream()
                .map(PointResponse::from)
                .toList();
    }
}
