package dev.tripdraw.trip.dto;

import dev.tripdraw.trip.query.TripPaging;

public record TripSearchRequest(

        TripSearchConditions condition,
        TripSearchPaging paging
) {

    public TripPaging toTripPaging() {
        return paging.toTripPaging();
    }
}
