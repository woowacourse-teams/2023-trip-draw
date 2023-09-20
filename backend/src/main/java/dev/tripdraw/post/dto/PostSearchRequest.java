package dev.tripdraw.post.dto;

import java.util.Set;
import lombok.Builder;

@Builder
public record PostSearchRequest(
        Set<Integer> years,
        Set<Integer> months,
        Set<Integer> daysOfWeek,
        Set<Integer> hours,
        Set<Integer> ageRanges,
        Set<Integer> genders,
        String address,
        Long lastViewedId,
        Integer limit
) {

    public PostSearchConditions toPostSearchConditions() {
        return PostSearchConditions.builder()
                .years(years)
                .months(months)
                .daysOfWeek(daysOfWeek)
                .hours(hours)
                .ageRanges(ageRanges)
                .genders(genders)
                .address(address)
                .build();
    }

    public PostSearchPaging toPostSearchPaging() {
        return new PostSearchPaging(lastViewedId, limit);
    }
}
