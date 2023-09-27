package dev.tripdraw.test.fixture;

import java.util.Map;
import java.util.Set;

public class TripSearchQueryParamsFixture {

    public static Map<String, Object> limitParams(int limit) {
        return Map.of("limit", limit);
    }

    public static Map<String, Object> lastViewedIdAndLimitParams(Long lastViewedId, int limit) {
        return Map.of(
                "lastViewedId", lastViewedId,
                "limit", limit
        );
    }

    public static Map<String, Object> yearsAndLimitParams(Set<Integer> years, int limit) {
        return Map.of(
                "years", years,
                "limit", limit
        );
    }

    public static Map<String, Object> monthsAndLimitParams(Set<Integer> months, int limit) {
        return Map.of(
                "months", months,
                "limit", limit
        );
    }

    public static Map<String, Object> daysOfWeekAndLimitParams(Set<Integer> daysOfWeek, int limit) {
        return Map.of(
                "daysOfWeek", daysOfWeek,
                "limit", limit
        );
    }

    public static Map<String, Object> ageRangesAndLimitParams(Set<Integer> ageRanges, int limit) {
        return Map.of(
                "ageRanges", ageRanges,
                "limit", limit
        );
    }

    public static Map<String, Object> gendersAndLimitParams(Set<Integer> genders, int limit) {
        return Map.of(
                "genders", genders,
                "limit", limit
        );
    }

    public static Map<String, Object> addressAndLimitParams(String address, int limit) {
        return Map.of(
                "address", address,
                "limit", limit
        );
    }
}
