package dev.tripdraw.trip.domain;

import static java.util.Set.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.trip.exception.TripException;
import dev.tripdraw.trip.query.TripQueryConditions;
import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TripQueryConditionsTest {

    @ParameterizedTest
    @ValueSource(ints = {2009, 2024})
    void 연도가_2010_미만_2023_초과이면_예외를_던진다(int year) {
        // given
        Set<Integer> years = of(year);

        // expect
        assertThatThrownBy(() -> new TripQueryConditions(years, of(), of(), of(), of(), ""))
                .isInstanceOf(TripException.class)
                .hasMessage("유효하지 않은 여행 조회 조건입니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 13})
    void 월이_1_미만_12_초과이면_예외를_던진다(int month) {
        // given
        Set<Integer> months = of(month);

        // expect
        assertThatThrownBy(() -> new TripQueryConditions(of(), months, of(), of(), of(), ""))
                .isInstanceOf(TripException.class)
                .hasMessage("유효하지 않은 여행 조회 조건입니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 8})
    void 요일이_1_미만_7_초과이면_예외를_던진다(int dayOfWeek) {
        // given
        Set<Integer> daysOfWeek = of(dayOfWeek);

        // expect
        assertThatThrownBy(() -> new TripQueryConditions(of(), of(), daysOfWeek, of(), of(), ""))
                .isInstanceOf(TripException.class)
                .hasMessage("유효하지 않은 여행 조회 조건입니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 11})
    void 연령대가_1_미만_10_초과이면_예외를_던진다(int ageRange) {
        // given
        Set<Integer> ageRanges = of(ageRange);

        // expect
        assertThatThrownBy(() -> new TripQueryConditions(of(), of(), of(), ageRanges, of(), ""))
                .isInstanceOf(TripException.class)
                .hasMessage("유효하지 않은 여행 조회 조건입니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 3})
    void 성별이_1_미만_2_초과이면_예외를_던진다(int gender) {
        // given
        Set<Integer> genders = of(gender);

        // expect
        assertThatThrownBy(() -> new TripQueryConditions(genders, of(), of(), of(), of(), ""))
                .isInstanceOf(TripException.class)
                .hasMessage("유효하지 않은 여행 조회 조건입니다.");
    }
}
