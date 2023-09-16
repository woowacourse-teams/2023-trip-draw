package dev.tripdraw.trip.dto;

import dev.tripdraw.trip.domain.SearchConditions;
import java.util.Set;

public record TripSearchConditions(
        Set<Integer> years,
        Set<Integer> months,
        Set<Integer> daysOfWeek,
        Set<Integer> ageRanges,
        Set<Integer> genders,
        String address
) {

    public SearchConditions toSearchConditions() {
        return new SearchConditions(
                years,
                months,
                daysOfWeek,
                ageRanges,
                genders,
                address
        );
    }
}
