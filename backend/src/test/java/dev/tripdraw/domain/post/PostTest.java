package dev.tripdraw.domain.post;

import static org.assertj.core.api.Assertions.assertThat;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.trip.Point;
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
        Post post = new Post("제목", point, "위치", "오늘은 날씨가 좋네요.", member);

        // expect
        assertThat(post.pointRecordedAt()).isEqualTo(recordedAt);
    }
}
