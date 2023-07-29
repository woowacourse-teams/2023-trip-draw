package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Trip;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

public record TripsSearchResponse(
        @Schema(description = "여행 목록")
        List<TripSearchResponse> trips
) {

    public static TripsSearchResponse from(List<Trip> trips) {
        return new TripsSearchResponse(generateTripSearchResponses(trips));
    }

    private static List<TripSearchResponse> generateTripSearchResponses(List<Trip> trips) {
        return trips.stream()
                .map(TripSearchResponse::from)
                .collect(Collectors.collectingAndThen(Collectors.toList(), List::copyOf));
    }
}
