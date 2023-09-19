package dev.tripdraw.trip.dto;

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
}
