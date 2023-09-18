package dev.tripdraw.post.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PostSearchConditions(
        List<Integer> years,
        List<Integer> months,
        List<Integer> daysOfWeek,
        List<Integer> hours,
        List<Integer> ageRanges,
        List<Integer> genders,
        String address
) {
}
