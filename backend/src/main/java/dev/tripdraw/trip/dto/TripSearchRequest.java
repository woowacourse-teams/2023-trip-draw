package dev.tripdraw.trip.dto;

import dev.tripdraw.trip.query.TripPaging;
import dev.tripdraw.trip.query.TripQueryConditions;

public record TripSearchRequest(
        TripSearchConditions condition,
        TripSearchPaging paging
) {

    public TripQueryConditions toTripQueryConditions() {
        return condition.toTripQueryConditions();
    }

    public TripPaging toTripPaging() {
        return paging.toTripPaging();
    }
}
