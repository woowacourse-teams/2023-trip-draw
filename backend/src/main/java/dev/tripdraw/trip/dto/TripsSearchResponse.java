package dev.tripdraw.trip.dto;

import java.util.List;

public record TripsSearchResponse(
        List<TripSearchResponse> trips,
        boolean hasNextPage
) {

    public static TripsSearchResponse of(List<TripSearchResponse> tripSearchResponses, boolean hasNextPage) {
        return new TripsSearchResponse(tripSearchResponses, hasNextPage);
    }
}
