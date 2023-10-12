package dev.tripdraw.post.application;

import static dev.tripdraw.post.application.PostServiceTest.PostRequestFixture.감상_생성_요청;
import static dev.tripdraw.post.application.PostServiceTest.PostRequestFixture.현재_위치_감상_생성_요청;
import static dev.tripdraw.post.exception.PostExceptionType.POST_NOT_FOUND;
import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.PostFixture.새로운_감상;
import static dev.tripdraw.trip.exception.TripExceptionType.POINT_NOT_FOUND;
import static dev.tripdraw.trip.exception.TripExceptionType.TRIP_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.post.dto.PostAndPointCreateRequest;
import dev.tripdraw.post.dto.PostCreateResponse;
import dev.tripdraw.post.dto.PostRequest;
import dev.tripdraw.post.dto.PostResponse;
import dev.tripdraw.post.dto.PostSearchRequest;
import dev.tripdraw.post.dto.PostSearchResponse;
import dev.tripdraw.post.dto.PostUpdateRequest;
import dev.tripdraw.post.dto.PostsResponse;
import dev.tripdraw.post.dto.PostsSearchResponse;
import dev.tripdraw.post.exception.PostException;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.exception.TripException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private RouteImageGenerator routeImageGenerator;

    @MockBean
    private PostFileUploader postFileUploader;

    private Member member;
    private Trip trip;
    private Point point;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(사용자());
        trip = tripRepository.save(Trip.of(member.id(), member.nickname()));
        point = pointRepository.save(새로운_위치정보(trip));
    }

    @Test
    void 현재_위치에_대한_감상을_생성한다() {
        // given
        PostAndPointCreateRequest request = 현재_위치_감상_생성_요청(trip.id());

        // when
        PostCreateResponse postCreateResponse = postService.addAtCurrentPoint(member.id(), request, trip, null);

        // then
        assertThat(postCreateResponse.postId()).isNotNull();
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성한다() {
        // given
        PostRequest postRequest = 감상_생성_요청(trip.id(), point.id());
        given(routeImageGenerator.generate(any(), any(), any(), any())).willReturn("hello.png");

        // when
        PostCreateResponse postCreateResponse = postService.addAtExistingLocation(member.id(), postRequest, trip, null);

        // then
        assertThat(postCreateResponse.postId()).isNotNull();
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_존재하지_않는_위치의_ID이면_예외를_발생시킨다() {
        // given
        PostRequest postRequest = 감상_생성_요청(trip.id(), Long.MIN_VALUE);

        // expect
        assertThatThrownBy(() -> postService.addAtExistingLocation(member.id(), postRequest, trip, null))
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_NOT_FOUND.message());
    }

    @Test
    void 특정_감상을_조회한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id(), "제주특별자치도 제주시 애월읍", trip.id()));

        // when
        PostResponse postResponse = postService.read(post.id());

        // then
        assertThat(postResponse).isEqualTo(PostResponse.from(post));
    }

    @Test
    void 특정_감상을_조회할_때_존재하지_않는_감상_ID이면_예외를_발생시킨다() {
        // expect
        assertThatThrownBy(() -> postService.read(Long.MIN_VALUE))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.message());
    }

    @Test
    void 특정_여행의_모든_감상을_조회한다() {
        // given
        Point point1 = pointRepository.save(새로운_위치정보(2023, 7, 12, 15, 29));
        Point point2 = pointRepository.save(새로운_위치정보(2023, 7, 12, 15, 30));
        Post post1 = postRepository.save(새로운_감상(point1, member.id(), "제주특별자치도 제주시 애월읍", trip.id()));
        Post post2 = postRepository.save(새로운_감상(point2, member.id(), "제주특별자치도 제주시 애월읍", trip.id()));

        // when
        PostsResponse postsResponse = postService.readAllByTripId(trip.id());

        // then
        assertThat(postsResponse.posts())
                .containsExactly(PostResponse.from(post2), PostResponse.from(post1));
    }

    @Test
    void 특정_여행의_모든_감상을_조회할_때_존재하지_않는_여행_ID이면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> postService.readAllByTripId(Long.MIN_VALUE))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.message());
    }

    @Test
    void 주소로_모든_여행을_조회한다() {
        // given
        Point mayPoint = pointRepository.save(새로운_위치정보(2023, 5, 12, 15, 30));
        Point julyPoint1 = pointRepository.save(새로운_위치정보(2023, 7, 12, 15, 30));
        Point julyPoint2 = pointRepository.save(새로운_위치정보(2023, 7, 12, 15, 30));
        Post jejuMayPost = postRepository.save(새로운_감상(mayPoint, member.id(), "제주특별자치도 제주시 애월읍"));
        Post jejuJulyPost = postRepository.save(새로운_감상(julyPoint1, member.id(), "제주특별자치도 제주시 애월읍"));
        Post seoulJulyPost = postRepository.save(새로운_감상(julyPoint2, member.id(), "서울특별시 송파구 문정동"));

        PostSearchRequest postSearchRequest = PostSearchRequest.builder()
                .address("제주특별자치도 제주시 애월읍")
                .limit(10)
                .build();

        // when
        PostsSearchResponse postsSearchResponse = postService.readAll(postSearchRequest);

        // then
        assertThat(postsSearchResponse.posts())
                .contains(PostSearchResponse.from(jejuJulyPost), PostSearchResponse.from(jejuMayPost));
    }

    @Test
    void 기간으로_진행된_여행을_조회한다() {
        // given
        Point mayPoint = pointRepository.save(새로운_위치정보(2023, 5, 12, 15, 30));
        Point julyPoint1 = pointRepository.save(새로운_위치정보(2023, 7, 12, 15, 30));
        Point julyPoint2 = pointRepository.save(새로운_위치정보(2023, 7, 12, 15, 30));
        Post jejuMayPost = postRepository.save(새로운_감상(mayPoint, member.id(), "제주특별자치도 제주시 애월읍"));
        Post jejuJulyPost = postRepository.save(새로운_감상(julyPoint1, member.id(), "제주특별자치도 제주시 애월읍"));
        Post seoulJulyPost = postRepository.save(새로운_감상(julyPoint2, member.id(), "서울특별시 송파구 문정동"));

        PostSearchRequest postSearchRequest = PostSearchRequest.builder()
                .months(Set.of(7))
                .limit(10)
                .build();

        // when
        PostsSearchResponse postsSearchResponse = postService.readAll(postSearchRequest);

        // then
        assertThat(postsSearchResponse.posts())
                .containsExactly(PostSearchResponse.from(seoulJulyPost), PostSearchResponse.from(jejuJulyPost));
    }

    @Test
    void 감상을_수정한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id(), "", trip.id()));
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("우도의 땅콩 아이스크림", "수정한 내용입니다.");
        String imageUrl = "image.jpeg";

        // when
        postService.update(postUpdateRequest, post, trip, imageUrl);

        // then
        Post updatedPost = postRepository.getByPostId(post.id());
        Trip updatedTrip = tripRepository.getById(trip.id());
        assertSoftly(softly -> {
            softly.assertThat(updatedPost.title()).isEqualTo("우도의 땅콩 아이스크림");
            softly.assertThat(updatedPost.writing()).isEqualTo("수정한 내용입니다.");
            softly.assertThat(updatedPost.postImageUrl()).isEqualTo("image.jpeg");
            softly.assertThat(updatedTrip.imageUrl()).isEqualTo("image.jpeg");
        });
    }

    @Test
    void 감상을_삭제한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id()));

        // when
        postService.delete(post);

        // then
        assertThatThrownBy(() -> postService.read(post.id()))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.message());
    }

    @Test
    void 이미지를_저장하는_경우_여행의_대표이미지도_변경한다() {
        // given
        PostAndPointCreateRequest request = 현재_위치_감상_생성_요청(trip.id());

        // when
        postService.addAtCurrentPoint(member.id(), request, trip, "hello.png");

        // then
        assertThat(trip.imageUrl()).isEqualTo("hello.png");
    }

    static class PostRequestFixture {
        public static PostAndPointCreateRequest 현재_위치_감상_생성_요청(Long tripId) {
            return new PostAndPointCreateRequest(
                    tripId,
                    "우도의 바닷가",
                    "제주특별자치도 제주시 애월읍",
                    "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                    1.1,
                    2.2,
                    LocalDateTime.of(2023, 7, 18, 20, 24)
            );
        }

        public static PostRequest 감상_생성_요청(Long tripId, Long pointId) {
            return new PostRequest(
                    tripId,
                    pointId,
                    "우도의 바닷가",
                    "제주특별자치도 제주시 애월읍",
                    "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
            );
        }
    }
}
