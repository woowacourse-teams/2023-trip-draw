package dev.tripdraw.post.domain;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.post.exception.PostExceptionType.POST_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import dev.tripdraw.common.config.JpaConfig;
import dev.tripdraw.common.config.QueryDslConfig;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.exception.PostException;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripName;
import dev.tripdraw.trip.domain.TripRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({JpaConfig.class, QueryDslConfig.class})
@AutoConfigureTestDatabase(replace = NONE)
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
        trip = tripRepository.save(new Trip(TripName.from("통후추의 여행"), member.id()));
        point = new Point(3.14, 5.25, LocalDateTime.now());
        trip.add(point);
    }

    @Test
    void 여행_ID로_감상_목록을_조회한다() {
        // given
        Post post = new Post("제목", point, "위치", "오늘은 날씨가 좋네요.", member.id(), trip.id());
        postRepository.save(post);

        // when
        List<Post> posts = postRepository.findAllByTripId(trip.id());

        // then
        assertThat(posts).usingRecursiveComparison().isEqualTo(List.of(post));
    }

    @Test
    void 회원_ID로_감상을_삭제한다() {
        // given
        Post post = new Post("제목", point, "위치", "오늘은 날씨가 좋네요.", member.id(), trip.id());

        Long postId = postRepository.save(post).id();

        // when
        postRepository.deleteByMemberId(member.id());

        // then
        assertThat(postRepository.existsById(postId)).isFalse();
    }

    @Test
    void 감상_ID로_감상을_조회한다() {
        // given
        Post post = postRepository.save(new Post("제목", point, "위치", "오늘은 날씨가 좋네요.", member.id(), trip.id()));

        // when
        Post foundPost = postRepository.getByPostId(post.id());

        // then
        assertThat(foundPost).isEqualTo(post);
    }

    @Test
    void 감상_ID로_감상을_조회할_때_존재하지_않는_경우_예외를_발생시킨다() {
        // given
        Long wrongId = Long.MIN_VALUE;

        // expect
        assertThatThrownBy(() -> postRepository.getByPostId(wrongId))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.message());
    }
}
