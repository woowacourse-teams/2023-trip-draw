package dev.tripdraw.domain.trip;

import dev.tripdraw.exception.trip.TripException;
import dev.tripdraw.exception.trip.TripExceptionType;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RouteLengthTest {

    @Test
    void 경로의_길이를_계산한다() {
        // given
        List<Point> points = List.of(new Point(1.1, 1.1, LocalDateTime.now()), new Point(2.1, 1.1, LocalDateTime.now()), new Point(2.1, 2.1, LocalDateTime.now()));

        // when
        RouteLength length = RouteLength.from(points);

        // then
        assertThat(length.lengthInKm()).isEqualTo("222.13km");
    }

    @ParameterizedTest
    @MethodSource("generateData")
    void 경로에_위치정보가_하나이거나_없는_경우_예외를_발생시킨다(List<Point> points) {
        // expect
        assertThatThrownBy(() -> RouteLength.from(points))
                .isInstanceOf(TripException.class)
                .hasMessage(TripExceptionType.ONE_OR_NO_POINT.message());
    }

    static Stream<List<Point>> generateData() {
        return Stream.of(
                new ArrayList<>(),
                List.of(new Point(1.1, 1.1, LocalDateTime.now()))
        );
    }


}
