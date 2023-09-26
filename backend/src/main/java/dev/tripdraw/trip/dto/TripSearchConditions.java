package dev.tripdraw.trip.dto;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import lombok.Builder;

@Builder
public record TripSearchConditions(
        Set<Integer> years,
        Set<Integer> months,
        Set<Integer> daysOfWeek,
        Set<Integer> ageRanges,
        Set<Integer> genders,
        String address
) {

    public boolean hasAllConditions() {
        return hasAddressCondition() && hasTimeConditions();
    }

    public boolean hasAddressCondition() {
        return Objects.nonNull(address) && !address.isBlank();
    }

    public boolean hasTimeConditions() {
        return Stream.of(years, months, daysOfWeek)
                .anyMatch(this::hasConditions);
    }

    private boolean hasConditions(Set<Integer> timeConditions) {
        return Objects.nonNull(timeConditions) && !timeConditions.isEmpty();
    }
}
