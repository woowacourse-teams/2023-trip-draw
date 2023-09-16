package dev.tripdraw.test.fixture;

import dev.tripdraw.trip.domain.SearchConditions;
import java.util.Set;


public class SearchConditionsFixture {

    public static SearchConditions emptySearchConditions() {
        return new SearchConditions(
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                ""
        );
    }

    public static SearchConditions yearsSearchConditions(Set<Integer> years) {
        return new SearchConditions(
                years,
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                ""
        );
    }

    public static SearchConditions monthsSearchConditions(Set<Integer> months) {
        return new SearchConditions(
                Set.of(),
                months,
                Set.of(),
                Set.of(),
                Set.of(),
                ""
        );
    }

    public static SearchConditions daysOfWeekSearchConditions(Set<Integer> daysOfWeek) {
        return new SearchConditions(
                Set.of(),
                Set.of(),
                daysOfWeek,
                Set.of(),
                Set.of(),
                ""
        );
    }

    public static SearchConditions ageRangesSearchConditions(Set<Integer> ageRanges) {
        return new SearchConditions(
                Set.of(),
                Set.of(),
                Set.of(),
                ageRanges,
                Set.of(),
                ""
        );
    }

    public static SearchConditions gendersSearchConditions(Set<Integer> genders) {
        return new SearchConditions(
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                genders,
                ""
        );
    }

    public static SearchConditions addressSearchConditions(String address) {
        return new SearchConditions(
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                Set.of(),
                address
        );
    }
}
