package dev.tripdraw.domain.trip;

import dev.tripdraw.exception.trip.TripException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static dev.tripdraw.exception.trip.TripExceptionType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        Point point = new Point(1L, 1.1, 2.2, false, LocalDateTime.now());
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
        Route route = new Route();
        Point point = new Point(1L, 1.1, 2.2, false, LocalDateTime.now());
        Point inexistentPoint = new Point(2L, 1.1, 2.2, false, LocalDateTime.now());

        route.add(point);

        // expect
        assertThatThrownBy(() -> route.deletePointById(inexistentPoint.id()))
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_NOT_IN_TRIP.message());
    }

    @Test
    void 이미_삭제된_위치정보를_삭제하면_예외를_발생시킨다() {
        // given
        Route route = new Route();
        Point point = new Point(1L, 1.1, 2.2, false, LocalDateTime.now());

        route.add(point);
        route.deletePointById(point.id());

        // expect
        assertThatThrownBy(() -> route.deletePointById(point.id()))
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_ALREADY_DELETED.message());
    }

    @Test
    void 위치를_ID로_조회한다() {
        // given
        Route route = new Route();
        Point point1 = new Point(1L, 1.1, 2.1, false, LocalDateTime.now());
        Point point2 = new Point(2L, 1.2, 2.2, false, LocalDateTime.now());
        route.add(point1);
        route.add(point2);

        // when
        Point foundPoint = route.findPointById(1L);

        // then
        assertThat(foundPoint).isEqualTo(point1);
    }

    @Test
    void 위치를_존재하지_않는_ID로_조회하면_예외가_발생한다() {
        // given
        Route route = new Route();
        Point point = new Point(1L, 1.1, 2.1, false, LocalDateTime.now());
        route.add(point);

        // expect
        assertThatThrownBy(() -> route.findPointById(2L))
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_NOT_FOUND.message());
    }

    @Test
    void 삭제된_위치정보는_반환하지_않는다() {
        // given
        Route route = new Route();
        Point point1 = new Point(1L, 1.1, 2.1, false, LocalDateTime.now());
        Point point2 = new Point(2L, 3.1, 4.1, false, LocalDateTime.now());
        route.add(point1);
        route.add(point2);

        // when
        route.deletePointById(1L);

        // then
        assertThat(route.points()).containsExactly(point2);
    }

    @Test
    void 경로의_거리를_계산한다() {
        // given
        Route route = new Route();
        route.add(new Point(1.1, 1.1, LocalDateTime.now()));
        route.add(new Point(1.1, 2.1, LocalDateTime.now()));
        route.add(new Point(2.1, 2.1, LocalDateTime.now()));

        // when
        RouteLength routeLength = route.calculateRouteLength();

        // then
        assertThat(routeLength.lengthInKm()).isEqualTo("222.18km");
    }

    @Test
    void 경로에_위치정보가_존재하지_않으면_거리를_계산할_때_예외를_발생시킨다() {
        // given
        Route route = new Route();

        // expect
        assertThatThrownBy(route::calculateRouteLength)
                .isInstanceOf(TripException.class)
                .hasMessage(ONE_OR_NO_POINT.message());
    }

    @Test
    void 경로에_위치정보가_하나만_존재하면_거리를_계산할_때_예외를_발생시킨다() {
        // given
        Route route = new Route();
        route.add(new Point(1.1, 1.1, LocalDateTime.now()));

        // expect
        assertThatThrownBy(route::calculateRouteLength)
                .isInstanceOf(TripException.class)
                .hasMessage(ONE_OR_NO_POINT.message());
    }
}

