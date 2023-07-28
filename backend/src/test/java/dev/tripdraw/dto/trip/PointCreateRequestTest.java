package dev.tripdraw.dto.trip;

import static org.assertj.core.api.Assertions.assertThat;

import dev.tripdraw.domain.trip.Point;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PointCreateRequestTest {

    @Test
    void 위치_객체로_변환한다() {
        // given
        PointCreateRequest request = new PointCreateRequest(
                1L,
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // when
        Point point = request.toPoint();

        // then
        assertThat(point).usingRecursiveComparison().isEqualTo(new Point(
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        ));
    }
}
