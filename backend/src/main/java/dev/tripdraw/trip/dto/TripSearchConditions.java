package dev.tripdraw.trip.dto;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import lombok.Builder;
import org.springframework.util.StringUtils;

@Builder
public record TripSearchConditions(
        Set<Integer> years,
        Set<Integer> months,
        Set<Integer> daysOfWeek,
        Set<Integer> ageRanges,
        Set<Integer> genders,
        String address
) {

    public boolean hasNoCondition() {
        return !(hasTimeConditions() || hasAddressCondition() || hasMemberConditions());
    }

    public boolean hasOnlyAddressCondition() {
        return hasAddressCondition() && !(hasTimeConditions() || hasMemberConditions());
    }

    public boolean hasOnlyTimeConditions() {
        return hasTimeConditions() && !(hasAddressCondition() || hasMemberConditions());
    }

    private boolean hasTimeConditions() {
        return Stream.of(years, months, daysOfWeek)
                .anyMatch(this::hasConditions);
    }

    private boolean hasMemberConditions() {
        return Stream.of(ageRanges, genders)
                .anyMatch(this::hasConditions);
    }

    private boolean hasAddressCondition() {
        return StringUtils.hasText(address);
    }

    private boolean hasConditions(Set<Integer> conditions) {
        return Objects.nonNull(conditions) && !conditions.isEmpty();
    }
}
