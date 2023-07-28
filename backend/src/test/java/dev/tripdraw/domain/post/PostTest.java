package dev.tripdraw.domain.post;

import static dev.tripdraw.exception.trip.TripExceptionType.NOT_AUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.trip.Point;
import dev.tripdraw.exception.post.PostException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PostTest {

    @Test
    void 위치를_가져온다() {
        // given
        LocalDateTime recordedAt = LocalDateTime.now();
        Point point = new Point(3.14, 5.25, recordedAt);
        Member member = new Member("통후추");
        Post post = new Post("제목", point, "위치", "오늘은 날씨가 좋네요.", member, 1L);

        // expect
        assertThat(post.pointRecordedAt()).isEqualTo(recordedAt);
    }

    @Test
    void 인가된_사용자는_예외가_발생하지_않는다() {
        // given
        LocalDateTime recordedAt = LocalDateTime.now();
        Point point = new Point(3.14, 5.25, recordedAt);
        Member member = new Member("통후추");
        Post post = new Post("제목", point, "위치", "오늘은 날씨가 좋네요.", member, 1L);

        // expect
        assertThatNoException().isThrownBy(() -> post.validateAuthorization(member));
    }

    @Test
    void 인가되지_않은_사용자는_예외가_발생한다() {
        // given
        LocalDateTime recordedAt = LocalDateTime.now();
        Point point = new Point(3.14, 5.25, recordedAt);
        Member member = new Member("통후추");
        Post post = new Post("제목", point, "위치", "오늘은 날씨가 좋네요.", member, 1L);

        // expect
        assertThatThrownBy(() -> post.validateAuthorization(new Member("순후추")))
                .isInstanceOf(PostException.class)
                .hasMessage(NOT_AUTHORIZED.getMessage());
    }
}
