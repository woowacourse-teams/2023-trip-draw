package dev.tripdraw.test.fixture;

import dev.tripdraw.trip.dto.TripSearchConditions;

import java.util.Set;

public class TripSearchConditionsFixture {

    public static TripSearchConditions emptyTripSearchConditions() {
        return TripSearchConditions.builder()
                .years(Set.of())
                .months(Set.of())
                .daysOfWeek(Set.of())
                .ageRanges(Set.of())
                .genders(Set.of())
                .address("")
                .build();
    }

    public static TripSearchConditions yearsTripSearchConditions(Set<Integer> years) {
        return TripSearchConditions.builder()
                .years(years)
                .months(Set.of())
                .daysOfWeek(Set.of())
                .ageRanges(Set.of())
                .genders(Set.of())
                .address("")
                .build();
    }

    public static TripSearchConditions monthsTripSearchConditions(Set<Integer> months) {
        return TripSearchConditions.builder()
                .years(Set.of())
                .months(months)
                .daysOfWeek(Set.of())
                .ageRanges(Set.of())
                .genders(Set.of())
                .address("")
                .build();
    }

    public static TripSearchConditions daysOfWeekTripSearchConditions(Set<Integer> daysOfWeek) {
        return TripSearchConditions.builder()
                .years(Set.of())
                .months(Set.of())
                .daysOfWeek(daysOfWeek)
                .ageRanges(Set.of())
                .genders(Set.of())
                .address("")
                .build();
    }

    public static TripSearchConditions ageRangesTripSearchConditions(Set<Integer> ageRanges) {
        return TripSearchConditions.builder()
                .years(Set.of())
                .months(Set.of())
                .daysOfWeek(Set.of())
                .ageRanges(ageRanges)
                .genders(Set.of())
                .address("")
                .build();
    }

    public static TripSearchConditions gendersTripSearchConditions(Set<Integer> genders) {
        return TripSearchConditions.builder()
                .years(Set.of())
                .months(Set.of())
                .daysOfWeek(Set.of())
                .ageRanges(Set.of())
                .genders(genders)
                .address("")
                .build();
    }

    public static TripSearchConditions addressTripSearchConditions(String address) {
        return TripSearchConditions.builder()
                .years(Set.of())
                .months(Set.of())
                .daysOfWeek(Set.of())
                .ageRanges(Set.of())
                .genders(Set.of())
                .address(address)
                .build();
    }
}
