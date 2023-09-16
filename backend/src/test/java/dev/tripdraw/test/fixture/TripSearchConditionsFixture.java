package dev.tripdraw.test.fixture;

import dev.tripdraw.trip.dto.TripSearchConditions;
import java.util.Set;

public class TripSearchConditionsFixture {

    public static TripSearchConditions emptyTripSearchConditions() {
        return new TripSearchConditions(
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                ""
        );
    }

    public static TripSearchConditions yearsTripSearchConditions(Set<Integer> years) {
        return new TripSearchConditions(
                years,
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                ""
        );
    }

    public static TripSearchConditions monthsTripSearchConditions(Set<Integer> months) {
        return new TripSearchConditions(
                Set.of(),
                months,
                Set.of(),
                Set.of(),
                Set.of(),
                ""
        );
    }

    public static TripSearchConditions daysOfWeekTripSearchConditions(Set<Integer> daysOfWeek) {
        return new TripSearchConditions(
                Set.of(),
                Set.of(),
                daysOfWeek,
                Set.of(),
                Set.of(),
                ""
        );
    }

    public static TripSearchConditions ageRangesTripSearchConditions(Set<Integer> ageRanges) {
        return new TripSearchConditions(
                Set.of(),
                Set.of(),
                Set.of(),
                ageRanges,
                Set.of(),
                ""
        );
    }

    public static TripSearchConditions gendersTripSearchConditions(Set<Integer> genders) {
        return new TripSearchConditions(
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                genders,
                ""
        );
    }

    public static TripSearchConditions addressTripSearchConditions(String address) {
        return new TripSearchConditions(
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                address
        );
    }
}
