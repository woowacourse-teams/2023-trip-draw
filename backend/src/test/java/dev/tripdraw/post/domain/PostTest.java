package dev.tripdraw.post.domain;

import static dev.tripdraw.post.exception.PostExceptionType.NOT_AUTHORIZED_TO_POST;
import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.PointFixture.위치정보;
import static dev.tripdraw.trip.exception.TripExceptionType.POINT_ALREADY_HAS_POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.member.domain.Member;
import dev.tripdraw.post.exception.PostException;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.exception.TripException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PostTest {

    @Test
    void 감상에_저장된_위치정보의_시간을_가져온다() {
        // given
        LocalDateTime recordedAt = LocalDateTime.now();
        Post post = new Post("제목", 새로운_위치정보(recordedAt), "위치", "오늘은 날씨가 좋네요.", 사용자(), 1L);

        // expect
        assertThat(post.pointRecordedAt()).isEqualTo(recordedAt);
    }

    @Test
    void 사용자의_감상인지_확인한다() {
        // given
        Member member = 사용자();
        Post post = new Post("제목", 위치정보(), "위치", "오늘은 날씨가 좋네요.", member, 1L);

        // expect
        assertThatNoException().isThrownBy(() -> post.validateAuthorization(member.id()));
    }

    @Test
    void 사용자의_감상이_아니라면_예외가_발생한다() {
        // given
        Post post = new Post("제목", 위치정보(), "위치", "오늘은 날씨가 좋네요.", 사용자(), 1L);

        // expect
        assertThatThrownBy(() -> post.validateAuthorization(Long.MAX_VALUE))
                .isInstanceOf(PostException.class)
                .hasMessage(NOT_AUTHORIZED_TO_POST.message());
    }

    @Test
    void 감상을_생성할_때_저장된_위치정보에_감상이_존재한다고_변경한다() {
        // given
        Point point = 위치정보();

        // when
        new Post("제목", point, "위치", "오늘은 날씨가 좋네요.", 사용자(), 1L);

        // then
        assertThat(point.hasPost()).isTrue();
    }

    @Test
    void 감상을_생성할_때_감상의_위치에_이미_감상이_등록되어_있다면_예외가_발생한다() {
        // given
        Point point = 위치정보();
        point.registerPost();

        // expect
        assertThatThrownBy(() -> new Post("제목", point, "위치", "오늘은 날씨가 좋네요.", 사용자(), 1L))
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_ALREADY_HAS_POST.message());
    }

    @Test
    void 감상의_제목을_수정한다() {
        // given
        Post post = new Post("제목", 위치정보(), "위치", "오늘은 날씨가 좋네요.", 사용자(), 1L);

        // when
        post.changeTitle("바뀐 제목");

        // then
        assertThat(post.title()).isEqualTo("바뀐 제목");
    }

    @Test
    void 감상의_내용을_수정한다() {
        // given
        Post post = new Post("제목", 위치정보(), "위치", "오늘은 날씨가 좋네요.", 사용자(), 1L);

        // when
        post.changeWriting("내일은 바람이 많네요.");

        // then
        assertThat(post.writing()).isEqualTo("내일은 바람이 많네요.");
    }

    @Test
    void 감상_사진_URL을_변경한다() {
        // given
        Post post = new Post("제목", 위치정보(), "위치", "오늘은 날씨가 좋네요.", 사용자(), 1L);

        // when
        post.changePostImageUrl("/통후추셀카.jpg");

        // then
        assertThat(post.postImageUrl()).isEqualTo("/통후추셀카.jpg");
    }

    @Test
    void 경로_이미지_URL을_변경한다() {
        // given
        Post post = new Post("제목", 위치정보(), "위치", "오늘은 날씨가 좋네요.", 사용자(), 1L);

        // when
        post.changeRouteImageUrl("/통후추여행경로.png");

        // then
        assertThat(post.routeImageUrl()).isEqualTo("/통후추여행경로.png");
    }

    @Test
    void 감상의_사진_URL을_제거한다() {
        // given
        Post post = new Post("제목", 위치정보(), "위치", "오늘은 날씨가 좋네요.", 사용자(), 1L);
        post.changePostImageUrl("example.url");

        // when
        post.removePostImageUrl();

        // then
        assertThat(post.postImageUrl()).isNull();
    }
}
