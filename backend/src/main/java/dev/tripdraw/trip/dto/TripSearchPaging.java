package dev.tripdraw.trip.dto;

import dev.tripdraw.trip.query.TripPaging;

public record TripSearchPaging(Long lastViewedId, Integer limit) {
    public TripPaging toTripPaging() {
        return new TripPaging(lastViewedId, limit);
    }
}
