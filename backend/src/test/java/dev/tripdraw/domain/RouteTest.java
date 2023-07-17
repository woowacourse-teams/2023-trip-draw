package dev.tripdraw.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RouteTest {

    @Test
    void 경로에_좌표를_추가한다() {
        // given
        Route route = new Route();
        Point point = new Point(1.1, 2.2, LocalDateTime.now());

        // when
        route.add(point);

        // then
        assertThat(route.getPoints()).hasSize(1);
    }
}
