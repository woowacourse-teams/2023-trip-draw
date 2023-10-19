package dev.tripdraw.trip.dto.v1;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import dev.tripdraw.trip.dto.TripSearchResponse;
import java.util.List;

public record TripsSearchResponseV1(
        List<TripSearchResponseV1> trips,
        boolean hasNextPage
) {

    public static TripsSearchResponseV1 of(List<TripSearchResponse> tripSearchResponses, boolean hasNextPage) {
        return tripSearchResponses.stream()
                .map(TripSearchResponseV1::from)
                .collect(collectingAndThen(toList(), items -> new TripsSearchResponseV1(items, hasNextPage)));
    }
}
