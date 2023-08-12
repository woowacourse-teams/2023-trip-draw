package dev.tripdraw.domain.post;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static dev.tripdraw.exception.post.PostExceptionType.POST_NOT_FOUND;
import static java.lang.Long.MIN_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.trip.Point;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripName;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.exception.post.PostException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Trip trip;
    private Point point;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        trip = tripRepository.save(new Trip(TripName.from("통후추의 여행"), member));
        point = new Point(3.14, 5.25, LocalDateTime.now());
        trip.add(point);
    }

    @Test
    void 여행_ID로_감상_목록을_조회한다() {
        // given
        Post post = new Post("제목", point, "위치", "오늘은 날씨가 좋네요.", member, trip.id());
        postRepository.save(post);

        // when
        List<Post> posts = postRepository.findAllByTripId(trip.id());

        // then
        assertThat(posts).usingRecursiveComparison().isEqualTo(List.of(post));
    }

    @Test
    void 회원_ID로_감상을_삭제한다() {
        // given
        Post post = new Post("제목", point, "위치", "오늘은 날씨가 좋네요.", member, trip.id());
        postRepository.save(post);

        // when
        postRepository.deleteByMemberId(member.id());

        // then
        assertThat(postRepository.findById(post.id())).isEmpty();
    }

    @Test
    void 감상_ID로_감상을_조회한다() {
        // given
        Post post = postRepository.save(new Post("제목", point, "위치", "오늘은 날씨가 좋네요.", member, trip.id()));

        // when
        Post foundPost = postRepository.getById(post.id());

        // then
        assertThat(foundPost).isEqualTo(post);
    }

    @Test
    void 감상_ID로_감상을_조회할_때_존재하지_않는_경우_예외를_발생시킨다() {
        // given
        Long wrongId = MIN_VALUE;

        // expect
        assertThatThrownBy(() -> postRepository.getById(wrongId))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.message());
    }
}
