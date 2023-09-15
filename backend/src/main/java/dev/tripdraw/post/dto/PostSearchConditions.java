package dev.tripdraw.post.dto;

import dev.tripdraw.post.domain.SearchConditions;
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
    public SearchConditions toSearchConditions() {
        return SearchConditions.builder()
                .years(years)
                .months(months)
                .daysOfWeek(daysOfWeek)
                .hours(hours)
                .ageRanges(ageRanges)
                .genders(genders)
                .address(address)
                .build();
    }
}
