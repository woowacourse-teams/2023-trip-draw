package dev.tripdraw.post.domain;

import java.util.List;

public record SearchConditions(
        List<Integer> years,
        List<Integer> months,
        List<Integer> daysOfWeek,
        List<Integer> hours,
        List<Integer> ageRanges,
        List<Integer> genders,
        String address
) {
}
