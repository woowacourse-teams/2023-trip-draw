package dev.tripdraw.trip.dto;

import dev.tripdraw.trip.query.TripQueryConditions;
import lombok.Builder;

import java.util.Set;

@Builder
public record TripSearchConditions(
        Set<Integer> years,
        Set<Integer> months,
        Set<Integer> daysOfWeek,
        Set<Integer> ageRanges,
        Set<Integer> genders,
        String address
) {

    public TripQueryConditions toTripQueryConditions() {
        return TripQueryConditions.builder()
                .years(years)
                .months(months)
                .daysOfWeek(daysOfWeek)
                .ageRanges(ageRanges)
                .genders(genders)
                .address(address)
                .build();
    }
}
