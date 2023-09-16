package dev.tripdraw.test;

import dev.tripdraw.trip.domain.SearchConditions;
import java.util.List;


public class SearchConditionsTestFixture {

    public static SearchConditions emptySearchConditions() {
        return new SearchConditions(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                ""
        );
    }

    public static SearchConditions yearsSearchConditions(List<Integer> years) {
        return new SearchConditions(
                years,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                ""
        );
    }

    public static SearchConditions monthsSearchConditions(List<Integer> months) {
        return new SearchConditions(
                List.of(),
                months,
                List.of(),
                List.of(),
                List.of(),
                ""
        );
    }

    public static SearchConditions daysOfWeekSearchConditions(List<Integer> daysOfWeek) {
        return new SearchConditions(
                List.of(),
                List.of(),
                daysOfWeek,
                List.of(),
                List.of(),
                ""
        );
    }

    public static SearchConditions ageRangesSearchConditions(List<Integer> ageRanges) {
        return new SearchConditions(
                List.of(),
                List.of(),
                List.of(),
                ageRanges,
                List.of(),
                ""
        );
    }

    public static SearchConditions gendersSearchConditions(List<Integer> genders) {
        return new SearchConditions(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                genders,
                ""
        );
    }

    public static SearchConditions addressSearchConditions(String address) {
        return new SearchConditions(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                address
        );
    }
}
