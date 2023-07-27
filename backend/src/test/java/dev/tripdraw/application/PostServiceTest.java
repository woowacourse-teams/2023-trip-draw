package dev.tripdraw.application;

import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.post.PostRepository;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.dto.post.PostPointCreateRequest;
import dev.tripdraw.dto.post.PostResponse;
import dev.tripdraw.exception.member.MemberException;
import dev.tripdraw.exception.trip.TripException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Trip trip;
    private LoginUser loginUser;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(new Member("통후추"));
        trip = tripRepository.save(Trip.from(member));
        loginUser = new LoginUser("통후추");
    }


    // 정상 저장
    @Test
    void 현재_위치에_대한_감상을_생성한다() {
        // given
        PostPointCreateRequest postPointCreateRequest = new PostPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // when
        PostResponse postResponse = postService.createOfCurrentLocation(loginUser, postPointCreateRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(postResponse.postId()).isNotNull();
            softly.assertThat(postResponse.title()).isEqualTo("우도의 바닷가");
            softly.assertThat(postResponse.pointResponse().pointId()).isNotNull();
        });
    }

    // nickname에 대한 Member 없을 때 예외
    @Test
    void 현재_위치에_대한_감상을_생성할_때_존재하지_않는_사용자_닉네임이면_예외를_발생시킨다() {
        // given
        LoginUser wrongUser = new LoginUser("상한 통후추");
        PostPointCreateRequest requestOfNotExistedTripId = new PostPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        assertThatThrownBy(() -> postService.createOfCurrentLocation(wrongUser, requestOfNotExistedTripId))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }

    // tripId로 Trip 조회할 수 없을 때 예외
    @Test
    void 현재_위치에_대한_감상을_생성할_때_존재하지_않는_여행의_ID이면_예외를_발생시킨다() {
        // given
        PostPointCreateRequest requestOfNotExistedTripId = new PostPointCreateRequest(
                -1L,
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        assertThatThrownBy(() -> postService.createOfCurrentLocation(loginUser, requestOfNotExistedTripId))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.getMessage());
    }
}
