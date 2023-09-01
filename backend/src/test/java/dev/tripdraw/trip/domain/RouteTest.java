package dev.tripdraw.trip.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class RouteTest {

    @Test
    void 경로에_위치정보를_추가한다() {
        // given
        Route route = new Route();
        Point point = new Point(1.1, 2.2, LocalDateTime.now());

        // when
        route.add(point);

        // then
        assertThat(route.points()).hasSize(1);
    }

    @Nested
    class 위치정보_포함_여부를_확인할_때 {

        @Test
        void 위치정보_포함하면_참값을_반환한다() {
            // given
            Route route = new Route();
            Point point = new Point(1.1, 2.2, LocalDateTime.now());
            route.add(point);

            // expect
            assertThat(route.contains(point)).isTrue();
        }

        @Test
        void 위치정보를_포함하지_않으면_참값을_반환하지_않는다() {
            // given
            Route route = new Route();
            Point point = new Point(1.1, 2.2, LocalDateTime.now());

            // expect
            assertThat(route.contains(point)).isFalse();
        }
    }
}

