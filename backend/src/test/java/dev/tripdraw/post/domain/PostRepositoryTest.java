package dev.tripdraw.post.domain;

import static dev.tripdraw.post.exception.PostExceptionType.POST_NOT_FOUND;
import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.PostFixture.새로운_감상;
import static dev.tripdraw.test.fixture.TripFixture.새로운_여행;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import dev.tripdraw.common.config.JpaConfig;
import dev.tripdraw.common.config.QueryDslConfig;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.exception.PostException;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
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
    private MemberRepository memberRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private TripRepository tripRepository;

    private Member member;
    private Trip trip;
    private Point point;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(사용자());
        trip = tripRepository.save(새로운_여행(member));
        point = pointRepository.save(새로운_위치정보(trip));
    }

    @Test
    void 여행_ID로_위치정보와_회원을_포함한_감상_목록을_조회한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id(), "", trip.id()));

        // when
        List<Post> posts = postRepository.findAllPostWithPointAndMemberByTripId(trip.id());

        // then
        assertThat(posts).containsExactly(post);
    }

    @Test
    void 감상_ID로_위치정보와_회원을_포함한_감상을_조회한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id(), "", trip.id()));

        // when
        Post foundPost = postRepository.getPostWithPointAndMemberById(post.id());

        // then
        assertThat(foundPost).isEqualTo(post);
    }

    @Test
    void 감상_ID로_위치정보와_회원을_포함한_감상을_조회할_때_존재하지_않는_경우_예외를_발생시킨다() {
        // given
        Long invalidPostId = Long.MIN_VALUE;

        // expect
        assertThatThrownBy(() -> postRepository.getPostWithPointAndMemberById(invalidPostId))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.message());
    }

    @Test
    void 위치정보_ID로_위치정보와_회원을_포함한_감상을_조회한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id(), "", trip.id()));

        // when
        Post foundPost = postRepository.getPostWithPointAndMemberByPointId(point.id());

        // then
        assertThat(foundPost).isEqualTo(post);
    }

    @Test
    void 위치정보_ID로_위치정보와_회원을_포함한_감상을_조회할_때_존재하지_않는_경우_예외를_발생시킨다() {
        // given
        Long invalidPointId = Long.MIN_VALUE;

        // expect
        assertThatThrownBy(() -> postRepository.getPostWithPointAndMemberByPointId(invalidPointId))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.message());
    }

    @Test
    void 회원_ID로_감상을_삭제한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id(), "", trip.id()));

        // when
        postRepository.deleteByMemberId(member.id());

        // then
        assertThat(postRepository.existsById(post.id())).isFalse();
    }

    @Test
    void 여행_ID로_감상을_삭제한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id(), "", trip.id()));

        // when
        postRepository.deleteByTripId(trip.id());

        // then
        assertThat(postRepository.existsById(post.id())).isFalse();
    }
}
