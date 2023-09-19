package dev.tripdraw.trip.query;

import static dev.tripdraw.trip.exception.TripExceptionType.INVALID_TRIP_SEARCH;

import dev.tripdraw.trip.exception.TripException;
import java.util.HashSet;
import java.util.Set;

public record TripQueryConditions(
        Set<Integer> years,
        Set<Integer> months,
        Set<Integer> daysOfWeek,
        Set<Integer> ageRanges,
        Set<Integer> genders,
        String address
) {

    private static final int YEAR_MINIMUM = 2010;
    private static final int YEAR_MAXIMUM = 2023;
    private static final int MONTH_MINIMUM = 1;
    private static final int MONTH_MAXIMUM = 12;
    private static final int DAYS_OF_WEEK_MINIMUM = 1;
    private static final int DAYS_OF_WEEK_MAXIMUM = 7;
    private static final int AGE_RANGE_MINIMUM = 1;
    private static final int AGE_RANGE_MAXIMUM = 10;
    private static final int GENDER_MINIMUM = 1;
    private static final int GENDER_MAXIMUM = 2;

    public TripQueryConditions(
            Set<Integer> years,
            Set<Integer> months,
            Set<Integer> daysOfWeek,
            Set<Integer> ageRanges,
            Set<Integer> genders,
            String address
    ) {
//        validate(years, months, daysOfWeek, ageRanges, genders);
        this.years = new HashSet<>(years);
        this.months = new HashSet<>(months);
        this.daysOfWeek = new HashSet<>(daysOfWeek);
        this.ageRanges = new HashSet<>(ageRanges);
        this.genders = new HashSet<>(genders);
        this.address = address;
    }

    private void validate(
            Set<Integer> years,
            Set<Integer> months,
            Set<Integer> daysOfWeek,
            Set<Integer> ageRanges,
            Set<Integer> genders
    ) {
        validateRange(years, YEAR_MINIMUM, YEAR_MAXIMUM);
        validateRange(months, MONTH_MINIMUM, MONTH_MAXIMUM);
        validateRange(daysOfWeek, DAYS_OF_WEEK_MINIMUM, DAYS_OF_WEEK_MAXIMUM);
        validateRange(ageRanges, AGE_RANGE_MINIMUM, AGE_RANGE_MAXIMUM);
        validateRange(genders, GENDER_MINIMUM, GENDER_MAXIMUM);
    }

    private void validateRange(Set<Integer> conditions, int min, int max) {
        if (isOutOfRange(conditions, min, max)) {
            throw new TripException(INVALID_TRIP_SEARCH);
        }
    }

    private boolean isOutOfRange(Set<Integer> conditions, int min, int max) {
        return conditions.stream()
                .anyMatch(condition -> condition < min || condition > max);
    }
}
