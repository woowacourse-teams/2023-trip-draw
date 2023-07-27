package dev.tripdraw.domain.trip;

import static dev.tripdraw.exception.trip.TripExceptionType.POINT_ALREADY_DELETED;
import static dev.tripdraw.exception.trip.TripExceptionType.POINT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.exception.trip.TripException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class RouteTest {

    @Test
    void 경로에_좌표를_추가한다() {
        // given
        Route route = new Route();
        Point point = new Point(1.1, 2.2, LocalDateTime.now());

        // when
        route.add(point);

        // then
        assertThat(route.points()).hasSize(1);
    }

    @Test
    void 경로에서_위치정보를_삭제한다() {
        // given
        Route route = new Route();
        Point point = new Point(1L, 1.1, 2.2, LocalDateTime.now());
        route.add(point);
        Long id = point.id();

        // when
        route.deletePointById(id);

        // then
        assertThat(point.isDeleted()).isTrue();
    }

    @Test
    void 경로에_존재하지_않는_위치정보를_삭제하면_예외를_발생시킨다() {
        // given
        Route route1 = new Route();
        Point point1 = new Point(1L, 1.1, 2.2, LocalDateTime.now());
        Point point2 = new Point(2L, 1.1, 2.2, LocalDateTime.now());

        route1.add(point1);

        // expect
        assertThatThrownBy(() -> route1.deletePointById(point2.id()))
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_NOT_FOUND.getMessage());
    }

    @Test
    void 이미_삭제된_위치정보를_삭제하면_예외를_발생시킨다() {
        // given
        Route route1 = new Route();
        Point point1 = new Point(1L, 1.1, 2.2, LocalDateTime.now());

        route1.add(point1);
        route1.deletePointById(point1.id());

        // expect
        assertThatThrownBy(() -> route1.deletePointById(point1.id()))
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_ALREADY_DELETED.getMessage());
    }
}

