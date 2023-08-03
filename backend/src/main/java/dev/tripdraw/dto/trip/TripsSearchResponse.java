package dev.tripdraw.dto.trip;

import static java.util.stream.Collectors.collectingAndThen;

import dev.tripdraw.domain.trip.Trip;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

public record TripsSearchResponse(
        @Schema(description = "여행 목록")
        List<TripSearchResponse> trips
) {

    public static TripsSearchResponse from(List<Trip> trips) {
        return trips.stream()
                .map(TripSearchResponse::from)
                .collect(collectingAndThen(Collectors.toList(), TripsSearchResponse::new));
    }
}
