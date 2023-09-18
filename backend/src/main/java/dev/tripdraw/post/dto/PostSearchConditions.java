package dev.tripdraw.post.dto;

import lombok.Builder;

import java.util.Set;

@Builder
public record PostSearchConditions(
        Set<Integer> years,
        Set<Integer> months,
        Set<Integer> daysOfWeek,
        Set<Integer> hours,
        Set<Integer> ageRanges,
        Set<Integer> genders,
        String address
) {
}
