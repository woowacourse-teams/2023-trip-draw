package dev.tripdraw.test.fixture;

import dev.tripdraw.trip.query.TripQueryConditions;

import java.util.Set;


public class TripQueryConditionsFixture {

    public static TripQueryConditions emptySearchConditions() {
        return TripQueryConditions.builder()
                .years(Set.of())
                .months(Set.of())
                .daysOfWeek(Set.of())
                .ageRanges(Set.of())
                .genders(Set.of())
                .address("")
                .build();
    }

    public static TripQueryConditions yearsSearchConditions(Set<Integer> years) {
        return TripQueryConditions.builder()
                .years(years)
                .months(Set.of())
                .daysOfWeek(Set.of())
                .ageRanges(Set.of())
                .genders(Set.of())
                .address("")
                .build();
    }

    public static TripQueryConditions monthsSearchConditions(Set<Integer> months) {
        return TripQueryConditions.builder()
                .years(Set.of())
                .months(months)
                .daysOfWeek(Set.of())
                .ageRanges(Set.of())
                .genders(Set.of())
                .address("")
                .build();
    }

    public static TripQueryConditions daysOfWeekSearchConditions(Set<Integer> daysOfWeek) {
        return TripQueryConditions.builder()
                .years(Set.of())
                .months(Set.of())
                .daysOfWeek(daysOfWeek)
                .ageRanges(Set.of())
                .genders(Set.of())
                .address("")
                .build();
    }

    public static TripQueryConditions ageRangesSearchConditions(Set<Integer> ageRanges) {
        return TripQueryConditions.builder()
                .years(Set.of())
                .months(Set.of())
                .daysOfWeek(Set.of())
                .ageRanges(ageRanges)
                .genders(Set.of())
                .address("")
                .build();
    }

    public static TripQueryConditions gendersSearchConditions(Set<Integer> genders) {
        return TripQueryConditions.builder()
                .years(Set.of())
                .months(Set.of())
                .daysOfWeek(Set.of())
                .ageRanges(Set.of())
                .genders(genders)
                .address("")
                .build();
    }

    public static TripQueryConditions addressSearchConditions(String address) {
        return TripQueryConditions.builder()
                .years(Set.of())
                .months(Set.of())
                .daysOfWeek(Set.of())
                .ageRanges(Set.of())
                .genders(Set.of())
                .address(address)
                .build();
    }
}
