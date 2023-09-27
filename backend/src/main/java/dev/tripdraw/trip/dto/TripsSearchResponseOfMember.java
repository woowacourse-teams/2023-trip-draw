package dev.tripdraw.trip.dto;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import dev.tripdraw.trip.domain.Trip;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record TripsSearchResponseOfMember(
        @Schema(description = "여행 목록")
        List<TripSearchResponseOfMember> trips
) {

    public static TripsSearchResponseOfMember from(List<Trip> trips) {
        return trips.stream()
                .map(TripSearchResponseOfMember::from)
                .collect(collectingAndThen(toList(), TripsSearchResponseOfMember::new));
    }
}
