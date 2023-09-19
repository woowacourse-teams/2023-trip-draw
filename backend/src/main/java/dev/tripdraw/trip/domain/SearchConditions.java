package dev.tripdraw.trip.domain;

import java.util.List;

public record SearchConditions(
        List<Integer> years,
        List<Integer> months,
        List<Integer> daysOfWeek,
        List<Integer> ageRanges,
        List<Integer> genders,
        String address
) {
}
