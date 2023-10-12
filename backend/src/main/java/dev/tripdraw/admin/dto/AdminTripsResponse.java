package dev.tripdraw.admin.dto;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import dev.tripdraw.trip.domain.Trip;
import java.util.List;

public record AdminTripsResponse(
        List<AdminTripResponse> items,
        boolean hasNextPage
) {
    public static AdminTripsResponse of(List<Trip> trips, boolean hasNextPage) {
        return trips.stream()
                .map(AdminTripResponse::from)
                .collect(collectingAndThen(toList(), items -> new AdminTripsResponse(items, hasNextPage)));
    }
}
