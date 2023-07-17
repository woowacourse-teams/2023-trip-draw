package dev.tripdraw.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TripTest {

    @Test
    void 여행_경로에_좌표를_추가한다() {
        // given
        Member member = new Member("tonghuchu");
        Trip trip = new Trip("trip", member);
        Point point = new Point(1.1, 2.2, LocalDateTime.now());

        // when
        trip.add(point);

        // then
        Route route = trip.getRoute();
        assertThat(route.getPoints()).hasSize(1);
    }
}
