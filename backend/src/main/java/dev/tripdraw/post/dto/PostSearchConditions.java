package dev.tripdraw.post.dto;

import dev.tripdraw.post.domain.SearchConditions;

import java.util.List;

public record PostSearchConditions(
        List<Integer> years,
        List<Integer> months,
        List<Integer> daysOfWeek,
        List<Integer> hours,
        List<Integer> ageRanges,
        List<Integer> genders,
        String address
) {
    public SearchConditions toSearchConditions() {
        return new SearchConditions(
                years,
                months,
                daysOfWeek,
                hours,
                ageRanges,
                genders,
                address
        );
    }
}
