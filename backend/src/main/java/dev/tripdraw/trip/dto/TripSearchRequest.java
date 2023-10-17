package dev.tripdraw.trip.dto;

import java.util.Set;
import lombok.Builder;

@Builder
public record TripSearchRequest(
        Set<Integer> years,
        Set<Integer> months,
        Set<Integer> daysOfWeek,
        Set<Integer> ageRanges,
        Set<Integer> genders,
        String address,
        Long lastViewedId,
        Integer limit
) {

    public TripSearchConditions toTripSearchConditions() {
        return TripSearchConditions.builder()
                .years(years)
                .months(months)
                .daysOfWeek(daysOfWeek)
                .ageRanges(ageRanges)
                .genders(genders)
                .address(address)
                .build();
    }

    public TripPaging toTripPaging() {
        return new TripPaging(lastViewedId, limit);
    }
}
