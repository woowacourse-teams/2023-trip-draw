package dev.tripdraw.test.fixture;

import dev.tripdraw.trip.dto.TripSearchConditions;

import java.util.Set;

public class TripSearchConditionsFixture {

    public static TripSearchConditions emptyTripSearchConditions() {
        return TripSearchConditions.builder()
                .build();
    }

    public static TripSearchConditions yearsTripSearchConditions(Set<Integer> years) {
        return TripSearchConditions.builder()
                .years(years)
                .build();
    }

    public static TripSearchConditions monthsTripSearchConditions(Set<Integer> months) {
        return TripSearchConditions.builder()
                .months(months)
                .build();
    }

    public static TripSearchConditions daysOfWeekTripSearchConditions(Set<Integer> daysOfWeek) {
        return TripSearchConditions.builder()
                .daysOfWeek(daysOfWeek)
                .build();
    }

    public static TripSearchConditions ageRangesTripSearchConditions(Set<Integer> ageRanges) {
        return TripSearchConditions.builder()
                .ageRanges(ageRanges)
                .build();
    }

    public static TripSearchConditions gendersTripSearchConditions(Set<Integer> genders) {
        return TripSearchConditions.builder()
                .genders(genders)
                .build();
    }

    public static TripSearchConditions addressTripSearchConditions(String address) {
        return TripSearchConditions.builder()
                .address(address)
                .build();
    }
}
