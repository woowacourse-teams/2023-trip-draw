package dev.tripdraw.trip.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TripNameTest {

    @Test
    void 이름을_변경한다() {
        // given
        TripName tripName = TripName.from("통후추");

        // when
        tripName.change("제주도 여행");

        // then
        assertThat(tripName.name()).isEqualTo("제주도 여행");
    }
}
