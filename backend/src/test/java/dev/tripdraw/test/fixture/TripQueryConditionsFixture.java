package dev.tripdraw.test.fixture;

import dev.tripdraw.trip.query.TripQueryConditions;
import java.util.Set;


public class TripQueryConditionsFixture {

    public static TripQueryConditions emptySearchConditions() {
        return new TripQueryConditions(
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                ""
        );
    }

    public static TripQueryConditions yearsSearchConditions(Set<Integer> years) {
        return new TripQueryConditions(
                years,
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                ""
        );
    }

    public static TripQueryConditions monthsSearchConditions(Set<Integer> months) {
        return new TripQueryConditions(
                Set.of(),
                months,
                Set.of(),
                Set.of(),
                Set.of(),
                ""
        );
    }

    public static TripQueryConditions daysOfWeekSearchConditions(Set<Integer> daysOfWeek) {
        return new TripQueryConditions(
                Set.of(),
                Set.of(),
                daysOfWeek,
                Set.of(),
                Set.of(),
                ""
        );
    }

    public static TripQueryConditions ageRangesSearchConditions(Set<Integer> ageRanges) {
        return new TripQueryConditions(
                Set.of(),
                Set.of(),
                Set.of(),
                ageRanges,
                Set.of(),
                ""
        );
    }

    public static TripQueryConditions gendersSearchConditions(Set<Integer> genders) {
        return new TripQueryConditions(
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                genders,
                ""
        );
    }

    public static TripQueryConditions addressSearchConditions(String address) {
        return new TripQueryConditions(
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                address
        );
    }
}
