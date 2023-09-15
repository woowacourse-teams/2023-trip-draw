package dev.tripdraw.post.domain;

import lombok.Builder;

import java.util.List;

@Builder
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
