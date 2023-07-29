package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Trip;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record TripsReadResponse(
        @Schema(description = "여행 목록")
        List<TripReadResponse> trips
) {

    public static TripsReadResponse from(List<Trip> trips) {
        return new TripsReadResponse(generateTripReadResponses(trips));
    }

    private static List<TripReadResponse> generateTripReadResponses(List<Trip> trips) {
        return trips.stream()
                .map(TripReadResponse::from)
                .toList();
    }
}
