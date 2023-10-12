package dev.tripdraw.common.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.tripdraw.trip.query.TripPaging;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TripPagingTest {

    @ParameterizedTest
    @CsvSource({"19, false", "20, false", "21, true"})
    void 다음_페이지가_있는지_확인한다(int size, boolean expected) {
        // given
        TripPaging tripPaging = new TripPaging(1L, 20);

        // when
        boolean actual = tripPaging.hasNextPage(size);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void limit이_최대값을_넘기면_최대값으로_조정된다() {
        // given
        int limit = 101;

        // when
        TripPaging tripPaging = new TripPaging(1L, limit);

        // then
        assertThat(tripPaging.limit()).isEqualTo(100);
    }

    @Test
    void limit이_null이면_기본값으로_조정된다() {
        // given
        Integer limit = null;

        // when
        TripPaging tripPaging = new TripPaging(1L, limit);

        // then
        assertThat(tripPaging.limit()).isEqualTo(20);
    }
}
