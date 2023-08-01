package dev.tripdraw.application;

import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.exception.post.PostExceptionType.POST_NOT_FOUNT;
import static dev.tripdraw.exception.trip.TripExceptionType.POINT_NOT_FOUND;
import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.trip.Point;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.dto.post.PostAndPointCreateRequest;
import dev.tripdraw.dto.post.PostCreateResponse;
import dev.tripdraw.dto.post.PostRequest;
import dev.tripdraw.dto.post.PostResponse;
import dev.tripdraw.dto.post.PostsResponse;
import dev.tripdraw.exception.member.MemberException;
import dev.tripdraw.exception.post.PostException;
import dev.tripdraw.exception.post.PostExceptionType;
import dev.tripdraw.exception.trip.TripException;
import dev.tripdraw.exception.trip.TripExceptionType;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Trip trip;
    private LoginUser loginUser;
    private LoginUser otherUser;
    private Point point;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(new Member("통후추"));
        memberRepository.save(new Member("순후추"));
        trip = tripRepository.save(Trip.from(member));
        point = new Point(1.1, 2.1, LocalDateTime.now());
        trip.add(point);
        tripRepository.flush();
        loginUser = new LoginUser("통후추");
        otherUser = new LoginUser("순후추");
    }

    @Test
    void 현재_위치에_대한_감상을_생성한다() {
        // given
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // when
        PostCreateResponse postCreateResponse = postService.addAtCurrentPoint(loginUser, postAndPointCreateRequest,
                null);

        // then
        assertThat(postCreateResponse.postId()).isNotNull();
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_존재하지_않는_사용자_닉네임이면_예외를_발생시킨다() {
        // given
        LoginUser wrongUser = new LoginUser("상한후추");
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        assertThatThrownBy(() -> postService.addAtCurrentPoint(wrongUser, postAndPointCreateRequest, null))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_존재하지_않는_여행의_ID이면_예외를_발생시킨다() {
        // given
        PostAndPointCreateRequest requestOfNotExistedTripId = new PostAndPointCreateRequest(
                Long.MIN_VALUE,
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        assertThatThrownBy(() -> postService.addAtCurrentPoint(loginUser, requestOfNotExistedTripId, null))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.getMessage());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성한다() {
        // given
        PostRequest postRequest = new PostRequest(
                trip.id(),
                point.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // when
        PostCreateResponse postCreateResponse = postService.addAtExistingLocation(loginUser, postRequest, null);

        // then
        assertThat(postCreateResponse.postId()).isNotNull();
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_존재하지_않는_사용자_닉네임이면_예외를_발생시킨다() {
        // given
        LoginUser wrongUser = new LoginUser("상한후추");
        PostRequest postRequest = new PostRequest(
                trip.id(),
                point.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        assertThatThrownBy(() -> postService.addAtExistingLocation(wrongUser, postRequest, null))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_존재하지_않는_여행의_ID이면_예외를_발생시킨다() {
        // given
        PostRequest requestOfNotExistedTripId = new PostRequest(
                Long.MIN_VALUE,
                point.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        assertThatThrownBy(() -> postService.addAtExistingLocation(loginUser, requestOfNotExistedTripId, null))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.getMessage());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_존재하지_않는_위치의_ID이면_예외를_발생시킨다() {
        // given
        PostRequest requestOfNotExistedPointId = new PostRequest(
                trip.id(),
                Long.MIN_VALUE,
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        assertThatThrownBy(() -> postService.addAtExistingLocation(loginUser, requestOfNotExistedPointId, null))
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_NOT_FOUND.getMessage());
    }

    @Test
    void 특정_감상을_조회한다() {
        // given
        PostCreateResponse postCreateResponse = createPost();

        // when
        PostResponse postResponse = postService.read(loginUser, postCreateResponse.postId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(postResponse.postId()).isNotNull();
            softly.assertThat(postResponse.title()).isEqualTo("우도의 바닷가");
            softly.assertThat(postResponse.pointResponse().pointId()).isNotNull();
        });
    }

    @Test
    void 특정_감상을_조회할_때_존재하지_않는_감상_ID이면_예외를_발생시킨다() {
        // given & expect
        assertThatThrownBy(() -> postService.read(loginUser, Long.MIN_VALUE))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUNT.getMessage());
    }

    @Test
    void 특정_감상을_조회할_때_존재하지_않는_사용자_닉네임이면_예외를_발생시킨다() {
        // given
        PostCreateResponse postCreateResponse = createPost();
        LoginUser wrongUser = new LoginUser("상한통후추");

        // expect
        assertThatThrownBy(() -> postService.read(wrongUser, postCreateResponse.postId()))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 특정_감상을_조회할_때_로그인_한_사용자가_감상의_작성자가_아니면_예외가_발생한다() {
        // given
        PostCreateResponse postCreateResponse = createPost();

        // expect
        assertThatThrownBy(() -> postService.read(otherUser, postCreateResponse.postId()))
                .isInstanceOf(PostException.class)
                .hasMessage(PostExceptionType.NOT_AUTHORIZED.getMessage());
    }

    @Test
    void 특정_여행의_모든_감상을_조회한다() {
        // given
        PostCreateResponse postCreateResponse1 = createPost();
        PostCreateResponse postCreateResponse2 = createPost();

        // when
        PostsResponse postsResponse = postService.readAllByTripId(loginUser, trip.id());
        List<PostResponse> posts = postsResponse.posts();

        // then
        assertSoftly(softly -> {
            softly.assertThat(posts.get(0).postId()).isNotNull();
            softly.assertThat(posts.get(0).title()).isEqualTo("우도의 바닷가");
            softly.assertThat(posts.get(0).pointResponse().pointId()).isNotNull();
            softly.assertThat(posts.get(1).postId()).isNotNull();
            softly.assertThat(posts.get(1).title()).isEqualTo("우도의 바닷가");
            softly.assertThat(posts.get(1).pointResponse().pointId()).isNotNull();
        });
    }

    @Test
    void 특정_여행의_모든_감상을_조회할_때_존재하지_않는_사용자_닉네임이면_예외를_발생시킨다() {
        // given
        LoginUser wrongUser = new LoginUser("상한 통후추");

        // expect
        assertThatThrownBy(() -> postService.readAllByTripId(wrongUser, trip.id()))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 특정_여행의_모든_감상을_조회할_때_존재하지_않는_여행_ID이면_예외가_발생한다() {
        // given & expect
        assertThatThrownBy(() -> postService.readAllByTripId(loginUser, Long.MIN_VALUE))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.getMessage());
    }

    @Test
    void 특정_여행의_모든_감상을_조회할_때_로그인_한_사용자가_여행의_주인이_아니면_예외가_발생한() {
        // given & expect
        assertThatThrownBy(() -> postService.readAllByTripId(otherUser, trip.id()))
                .isInstanceOf(TripException.class)
                .hasMessage(TripExceptionType.NOT_AUTHORIZED.getMessage());
    }

    private PostCreateResponse createPost() {
        PostAndPointCreateRequest postPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        return postService.addAtCurrentPoint(loginUser, postPointCreateRequest, null);
    }
}
