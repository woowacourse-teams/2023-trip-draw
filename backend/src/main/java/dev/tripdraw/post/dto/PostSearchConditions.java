package dev.tripdraw.post.dto;

import java.util.Set;
import lombok.Builder;

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
