package dev.tripdraw.post.dto;

import static org.assertj.core.api.Assertions.assertThat;

import dev.tripdraw.trip.domain.Point;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PostResponseAndPointCreateRequestTest {

    @Test
    void 위치_객체로_변환한다() {
        // given
        PostAndPointCreateRequest request = new PostAndPointCreateRequest(
                1L,
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // when
        Point point = request.toPoint();

        // then
        assertThat(point).usingRecursiveComparison().isEqualTo(
                new Point(1.1, 2.2, LocalDateTime.of(2023, 7, 18, 20, 24))
        );
    }
}
