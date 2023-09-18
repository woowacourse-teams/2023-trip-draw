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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.file.application.FileUploader;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.member.exception.MemberException;
import dev.tripdraw.post.dto.PostAndPointCreateRequest;
import dev.tripdraw.post.dto.PostCreateResponse;
import dev.tripdraw.post.dto.PostRequest;
import dev.tripdraw.post.dto.PostResponse;
import dev.tripdraw.post.dto.PostSearchConditions;
import dev.tripdraw.post.dto.PostSearchPaging;
import dev.tripdraw.post.dto.PostSearchRequest;
import dev.tripdraw.post.dto.PostSearchResponse;
import dev.tripdraw.post.dto.PostUpdateRequest;
import dev.tripdraw.post.dto.PostsResponse;
import dev.tripdraw.post.dto.PostsSearchResponse;
import dev.tripdraw.post.exception.PostException;
import dev.tripdraw.test.ServiceTest;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.exception.TripException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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

    @MockBean
    private FileUploader fileUploader;

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
        PostAndPointCreateRequest request = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // when
        PostCreateResponse postCreateResponse = postService.addAtCurrentPoint(loginUser, request, null);

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
                "제주특별자치도 제주시 애월읍",
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
                "제주특별자치도 제주시 애월읍",
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
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );
        given(routeImageGenerator.generate(any(), any(), any(), any())).willReturn("hello.png");

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
                "제주특별자치도 제주시 애월읍",
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
                "제주특별자치도 제주시 애월읍",
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
                "제주특별자치도 제주시 애월읍",
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
        PostCreateResponse postCreateResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.now());

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
        // expect
        assertThatThrownBy(() -> postService.read(loginUser, Long.MIN_VALUE))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.message());
    }

    @Test
    void 특정_감상을_조회할_때_존재하지_않는_사용자_닉네임이면_예외를_발생시킨다() {
        // given
        PostCreateResponse postCreateResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.now());
        LoginUser wrongUser = new LoginUser(Long.MIN_VALUE);

        // expect
        Long postId = postCreateResponse.postId();
        assertThatThrownBy(() -> postService.read(wrongUser, postId))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void 특정_감상을_조회할_때_로그인_한_사용자가_감상의_작성자가_아니면_예외가_발생한다() {
        // given
        PostCreateResponse postCreateResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.now());

        // expect
        Long postId = postCreateResponse.postId();
        assertThatThrownBy(() -> postService.read(otherUser, postId))
                .isInstanceOf(PostException.class)
                .hasMessage(NOT_AUTHORIZED_TO_POST.message());
    }

    @Test
    void 특정_여행의_모든_감상을_조회한다() {
        // given
        createPost("제주특별자치도 제주시 애월읍", LocalDateTime.now());
        createPost("제주특별자치도 제주시 애월읍", LocalDateTime.now());

        // when
        PostsResponse postsResponse = postService.readAllByTripId(loginUser, trip.id());

        // then
        List<PostResponse> posts = postsResponse.posts();
        assertSoftly(softly -> {
            softly.assertThat(posts.get(0).postId()).isNotNull();
            softly.assertThat(posts.get(0).pointResponse().pointId()).isNotNull();
            softly.assertThat(posts.get(1).postId()).isNotNull();
            softly.assertThat(posts.get(1).pointResponse().pointId()).isNotNull();
        });
    }

    @Test
    void 특정_여행의_모든_감상을_조회할_때_존재하지_않는_사용자_닉네임이면_예외를_발생시킨다() {
        // given
        LoginUser wrongUser = new LoginUser(Long.MIN_VALUE);

        // expect
        Long tripId = trip.id();
        assertThatThrownBy(() -> postService.readAllByTripId(wrongUser, tripId))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void 특정_여행의_모든_감상을_조회할_때_존재하지_않는_여행_ID이면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> postService.readAllByTripId(loginUser, Long.MIN_VALUE))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.message());
    }

    @Test
    void 특정_여행의_모든_감상을_조회할_때_로그인_한_사용자가_여행의_주인이_아니면_예외가_발생한다() {
        // expect
        Long tripId = trip.id();
        assertThatThrownBy(() -> postService.readAllByTripId(otherUser, tripId))
                .isInstanceOf(TripException.class)
                .hasMessage(NOT_AUTHORIZED_TO_TRIP.message());
    }

    @Test
    void 조건에_해당하는_모든_여행을_조회한다() {
        // given
        PostCreateResponse jejuMay = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.of(2023, 5, 12, 15, 30));
        PostCreateResponse jejuJuly = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.of(2023, 7, 12, 15, 30));
        PostCreateResponse seoulJuly = createPost("서울특별시 송파구 문정동", LocalDateTime.of(2023, 7, 12, 15, 30));

        PostSearchRequest postSearchRequestJeju = new PostSearchRequest(
                PostSearchConditions.builder()
                        .address("제주특별자치도 제주시 애월읍")
                        .build(),
                new PostSearchPaging(null, 10)
        );

        PostSearchRequest postSearchRequestJuly = new PostSearchRequest(
                PostSearchConditions.builder()
                        .months(List.of(7))
                        .build(),
                new PostSearchPaging(null, 10)
        );

        // when
        PostsSearchResponse postsSearchJejuResponse = postService.readAll(postSearchRequestJeju);
        PostsSearchResponse postsSearchJulyResponse = postService.readAll(postSearchRequestJuly);

        // then
        assertThat(postsSearchJejuResponse.posts().stream().map(PostSearchResponse::postId).toList()).containsExactly(jejuJuly.postId(), jejuMay.postId());
        assertThat(postsSearchJulyResponse.posts().stream().map(PostSearchResponse::postId).toList()).containsExactly(seoulJuly.postId(), jejuJuly.postId());

    }

    @Test
    void 감상을_수정한다() {
        // given
        PostCreateResponse postCreateResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.now());
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
        PostCreateResponse postCreateResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.now());
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                "우도의 땅콩 아이스크림",
                "수정한 내용입니다."
        );
        LoginUser wrongUser = new LoginUser(Long.MIN_VALUE);

        // expect
        Long postId = postCreateResponse.postId();
        assertThatThrownBy(() -> postService.update(wrongUser, postId, postUpdateRequest, null))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void 감상을_수정할_때_로그인_한_사용자가_감상의_작성자가_아니면_예외가_발생한다() {
        // given
        PostCreateResponse postCreateResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.now());
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                "우도의 땅콩 아이스크림",
                "수정한 내용입니다."
        );

        // expect
        Long postId = postCreateResponse.postId();
        assertThatThrownBy(() -> postService.update(otherUser, postId, postUpdateRequest, null))
                .isInstanceOf(PostException.class)
                .hasMessage(NOT_AUTHORIZED_TO_POST.message());
    }

    @Test
    void 감상을_삭제한다() {
        // given
        PostCreateResponse postCreateResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.now());

        // expect
        Long postId = postCreateResponse.postId();
        assertDoesNotThrow(() -> postService.delete(loginUser, postId));

        assertThatThrownBy(() -> postService.read(loginUser, postId))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.message());
    }

    @Test
    void 감상을_삭제할_때_존재하지_않는_감상_ID이면_예외를_발생시킨다() {
        // expect
        assertThatThrownBy(() -> postService.delete(loginUser, Long.MIN_VALUE))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.message());
    }

    @Test
    void 감상을_삭제할_때_존재하지_않는_사용자_닉네임이면_예외를_발생시킨다() {
        // given
        PostCreateResponse postCreateResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.now());
        LoginUser wrongUser = new LoginUser(Long.MIN_VALUE);

        // expect
        Long postId = postCreateResponse.postId();
        assertThatThrownBy(() -> postService.delete(wrongUser, postId))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void 감상을_삭제할_때_로그인_한_사용자가_감상의_작성자가_아니면_예외가_발생한다() {
        // given
        PostCreateResponse postCreateResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.now());

        // expect
        Long postId = postCreateResponse.postId();
        assertThatThrownBy(() -> postService.delete(otherUser, postId))
                .isInstanceOf(PostException.class)
                .hasMessage(NOT_AUTHORIZED_TO_POST.message());
    }

    @Test
    void 이미지를_저장하는_경우_여행의_대표이미지도_변경한다() {
        // given
        PostAndPointCreateRequest request = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );
        MultipartFile multipartFile = mock(MultipartFile.class);
        given(fileUploader.upload(any())).willReturn("hello.png");

        // when
        postService.addAtCurrentPoint(loginUser, request, multipartFile);

        // then
        assertThat(trip.imageUrl()).isEqualTo("hello.png");
    }

    private PostCreateResponse createPost(String address, LocalDateTime localDateTime) {
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                address,
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                localDateTime);
        return postService.addAtCurrentPoint(loginUser, postAndPointCreateRequest, null);
    }
}
