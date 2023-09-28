package dev.tripdraw.trip.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dev.tripdraw.trip.domain.Point;
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
        LocalDateTime recordedAt = LocalDateTime.of(2023, 7, 18, 20, 24);
        PointCreateRequest request = new PointCreateRequest(1L, 1.1, 2.2, recordedAt);

        // when
        Point point = request.toPoint();

        // then
        assertThat(point).usingRecursiveComparison().isEqualTo(new Point(1.1, 2.2, recordedAt));
    }
}
