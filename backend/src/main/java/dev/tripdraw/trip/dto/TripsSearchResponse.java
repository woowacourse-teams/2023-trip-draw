package dev.tripdraw.trip.dto;

import dev.tripdraw.trip.domain.Trip;
import java.util.List;

public record TripsSearchResponse(
        List<TripSearchResponse> trips,
        boolean hasNextPage
) {

    public static TripsSearchResponse of(List<Trip> trips, boolean hasNextPage) {
        List<TripSearchResponse> tripsSearchResponse = trips.stream()
                .map(TripSearchResponse::from)
                .toList();
        return new TripsSearchResponse(tripsSearchResponse, hasNextPage);
    }
}
