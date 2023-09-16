package dev.tripdraw.trip.dto;

import dev.tripdraw.common.dto.SearchPaging;

public record TripSearchRequest(
        TripSearchConditions condition,
        SearchPaging paging
) {
}
