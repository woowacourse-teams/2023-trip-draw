package dev.tripdraw.post.application;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.post.exception.PostExceptionType.NOT_AUTHORIZED_TO_POST;
import static dev.tripdraw.post.exception.PostExceptionType.POST_NOT_FOUND;
import static dev.tripdraw.trip.exception.TripExceptionType.NOT_AUTHORIZED_TO_TRIP;
import static dev.tripdraw.trip.exception.TripExceptionType.POINT_NOT_FOUND;
import static dev.tripdraw.trip.exception.TripExceptionType.TRIP_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;

import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.member.exception.MemberException;
import dev.tripdraw.post.dto.PostAndPointCreateRequest;
import dev.tripdraw.post.dto.PostCreateResponse;
import dev.tripdraw.post.dto.PostRequest;
import dev.tripdraw.post.dto.PostResponse;
import dev.tripdraw.post.dto.PostUpdateRequest;
import dev.tripdraw.post.dto.PostsResponse;
import dev.tripdraw.post.exception.PostException;
import dev.tripdraw.test.ServiceTest;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.exception.TripException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@ServiceTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private RouteImageGenerator routeImageGenerator;

    private Trip trip;
    private LoginUser loginUser;
    private LoginUser otherUser;
    private Point point;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        Member otherMember = memberRepository.save(new Member("순후추", "kakaoId", KAKAO));
        trip = tripRepository.save(Trip.from(member));
        point = new Point(1.1, 2.1, LocalDateTime.now());
        point.setTrip(trip);
        pointRepository.save(point);
        loginUser = new LoginUser(member.id());
        otherUser = new LoginUser(otherMember.id());
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
        LoginUser wrongUser = new LoginUser(Long.MIN_VALUE);
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
                .hasMessage(MEMBER_NOT_FOUND.message());
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
                .hasMessage(TRIP_NOT_FOUND.message());
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
        BDDMockito.given(routeImageGenerator.generate(any(), any(), any(), any())).willReturn("hello.png");

        // when
        PostCreateResponse postCreateResponse = postService.addAtExistingLocation(loginUser, postRequest, null);

        // then
        assertThat(postCreateResponse.postId()).isNotNull();
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_존재하지_않는_사용자_닉네임이면_예외를_발생시킨다() {
        // given
        LoginUser wrongUser = new LoginUser(Long.MIN_VALUE);
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
                .hasMessage(MEMBER_NOT_FOUND.message());
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
                .hasMessage(TRIP_NOT_FOUND.message());
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
                .hasMessage(POINT_NOT_FOUND.message());
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
                .hasMessage(POST_NOT_FOUND.message());
    }

    @Test
    void 특정_감상을_조회할_때_존재하지_않는_사용자_닉네임이면_예외를_발생시킨다() {
        // given
        PostCreateResponse postCreateResponse = createPost();
        LoginUser wrongUser = new LoginUser(Long.MIN_VALUE);

        // expect
        assertThatThrownBy(() -> postService.read(wrongUser, postCreateResponse.postId()))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void 특정_감상을_조회할_때_로그인_한_사용자가_감상의_작성자가_아니면_예외가_발생한다() {
        // given
        PostCreateResponse postCreateResponse = createPost();

        // expect
        assertThatThrownBy(() -> postService.read(otherUser, postCreateResponse.postId()))
                .isInstanceOf(PostException.class)
                .hasMessage(NOT_AUTHORIZED_TO_POST.message());
    }

    @Test
    void 특정_여행의_모든_감상을_조회한다() {
        // given
        createPost();
        createPost2();

        // when
        PostsResponse postsResponse = postService.readAllByTripId(loginUser, trip.id());

        // then
        List<PostResponse> posts = postsResponse.posts();
        assertSoftly(softly -> {
            softly.assertThat(posts.get(0).postId()).isNotNull();
            softly.assertThat(posts.get(0).title()).isEqualTo("우도의 땅콩 아이스크림");
            softly.assertThat(posts.get(0).pointResponse().pointId()).isNotNull();
            softly.assertThat(posts.get(1).postId()).isNotNull();
            softly.assertThat(posts.get(1).title()).isEqualTo("우도의 바닷가");
            softly.assertThat(posts.get(1).pointResponse().pointId()).isNotNull();
        });
    }

    @Test
    void 특정_여행의_모든_감상을_조회할_때_존재하지_않는_사용자_닉네임이면_예외를_발생시킨다() {
        // given
        LoginUser wrongUser = new LoginUser(Long.MIN_VALUE);

        // expect
        assertThatThrownBy(() -> postService.readAllByTripId(wrongUser, trip.id()))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void 특정_여행의_모든_감상을_조회할_때_존재하지_않는_여행_ID이면_예외가_발생한다() {
        // given & expect
        assertThatThrownBy(() -> postService.readAllByTripId(loginUser, Long.MIN_VALUE))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.message());
    }

    @Test
    void 특정_여행의_모든_감상을_조회할_때_로그인_한_사용자가_여행의_주인이_아니면_예외가_발생한다() {
        // given & expect
        assertThatThrownBy(() -> postService.readAllByTripId(otherUser, trip.id()))
                .isInstanceOf(TripException.class)
                .hasMessage(NOT_AUTHORIZED_TO_TRIP.message());
    }

    @Test
    void 감상을_수정한다() {
        // given
        PostCreateResponse postCreateResponse = createPost();
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                "우도의 땅콩 아이스크림",
                "수정한 내용입니다."
        );

        // when
        postService.update(loginUser, postCreateResponse.postId(), postUpdateRequest, null);

        // then
        PostResponse postResponseBeforeUpdate = postService.read(loginUser, postCreateResponse.postId());

        assertSoftly(softly -> {
            softly.assertThat(postResponseBeforeUpdate.postId()).isEqualTo(postCreateResponse.postId());
            softly.assertThat(postResponseBeforeUpdate.title()).isEqualTo("우도의 땅콩 아이스크림");
            softly.assertThat(postResponseBeforeUpdate.writing()).isEqualTo("수정한 내용입니다.");
        });
    }

    @Test
    void 감상을_수정할_때_존재하지_않는_감상_ID이면_예외를_발생시킨다() {
        // given
        PostCreateResponse postCreateResponse = createPost();
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                "우도의 땅콩 아이스크림",
                "수정한 내용입니다."
        );

        // expect
        assertThatThrownBy(() -> postService.update(loginUser, Long.MIN_VALUE, postUpdateRequest, null))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.message());
    }

    @Test
    void 감상을_수정할_때_존재하지_않는_사용자_닉네임이면_예외를_발생시킨다() {
        // given
        PostCreateResponse postCreateResponse = createPost();
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                "우도의 땅콩 아이스크림",
                "수정한 내용입니다."
        );
        LoginUser wrongUser = new LoginUser(Long.MIN_VALUE);

        // expect
        assertThatThrownBy(() -> postService.update(wrongUser, postCreateResponse.postId(), postUpdateRequest, null))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void 감상을_수정할_때_로그인_한_사용자가_감상의_작성자가_아니면_예외가_발생한다() {
        // given
        PostCreateResponse postCreateResponse = createPost();
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                "우도의 땅콩 아이스크림",
                "수정한 내용입니다."
        );

        // expect
        assertThatThrownBy(() -> postService.update(otherUser, postCreateResponse.postId(), postUpdateRequest, null))
                .isInstanceOf(PostException.class)
                .hasMessage(NOT_AUTHORIZED_TO_POST.message());
    }

    @Test
    void 감상을_삭제한다() {
        // given
        PostCreateResponse postCreateResponse = createPost();

        // expect
        assertDoesNotThrow(() -> postService.delete(loginUser, postCreateResponse.postId()));

        assertThatThrownBy(() -> postService.read(loginUser, postCreateResponse.postId()))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.message());
    }

    @Test
    void 감상을_삭제할_때_존재하지_않는_감상_ID이면_예외를_발생시킨다() {
        // given & expect
        assertThatThrownBy(() -> postService.delete(loginUser, Long.MIN_VALUE))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.message());
    }

    @Test
    void 감상을_삭제할_때_존재하지_않는_사용자_닉네임이면_예외를_발생시킨다() {
        // given
        PostCreateResponse postCreateResponse = createPost();
        LoginUser wrongUser = new LoginUser(Long.MIN_VALUE);

        // expect
        assertThatThrownBy(() -> postService.delete(wrongUser, postCreateResponse.postId()))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void 감상을_삭제할_때_로그인_한_사용자가_감상의_작성자가_아니면_예외가_발생한다() {
        // given
        PostCreateResponse postCreateResponse = createPost();

        // expect
        assertThatThrownBy(() -> postService.delete(otherUser, postCreateResponse.postId()))
                .isInstanceOf(PostException.class)
                .hasMessage(NOT_AUTHORIZED_TO_POST.message());
    }

    private PostCreateResponse createPost() {
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        return postService.addAtCurrentPoint(loginUser, postAndPointCreateRequest, null);
    }

    private PostCreateResponse createPost2() {
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 땅콩 아이스크림",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.2,
                2.2,
                LocalDateTime.of(2023, 7, 20, 12, 13)
        );

        return postService.addAtCurrentPoint(loginUser, postAndPointCreateRequest, null);
    }
}
