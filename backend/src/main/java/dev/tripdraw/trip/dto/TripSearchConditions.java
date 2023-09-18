package dev.tripdraw.trip.dto;

import dev.tripdraw.trip.query.TripQueryConditions;
import java.util.Set;
import lombok.Builder;

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
        return new TripQueryConditions(years(), months(), daysOfWeek(), ageRanges(), genders(), address());
    }
}
