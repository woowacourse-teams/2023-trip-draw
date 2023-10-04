package dev.tripdraw.trip.application;

import static dev.tripdraw.test.fixture.MemberFixture.다른_사용자;
import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.PostFixture.새로운_감상;
import static dev.tripdraw.test.fixture.TripFixture.새로운_여행;
import static dev.tripdraw.trip.domain.TripStatus.FINISHED;
import static dev.tripdraw.trip.exception.TripExceptionType.NOT_AUTHORIZED_TO_TRIP;
import static dev.tripdraw.trip.exception.TripExceptionType.TRIP_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.dto.TripCreateResponse;
import dev.tripdraw.trip.dto.TripResponse;
import dev.tripdraw.trip.dto.TripSearchRequest;
import dev.tripdraw.trip.dto.TripUpdateRequest;
import dev.tripdraw.trip.dto.TripsSearchResponse;
import dev.tripdraw.trip.dto.TripsSearchResponseOfMember;
import dev.tripdraw.trip.exception.TripException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
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
class TripServiceTest {

    @Autowired
    private TripService tripService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PointRepository pointRepository;

    @MockBean
    private RouteImageGenerator routeImageGenerator;

    @Autowired
    private PostRepository postRepository;

    private Trip trip;
    private Point point;
    private LoginUser loginUser;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(사용자());
        trip = tripRepository.save(새로운_여행(member));
        point = pointRepository.save(새로운_위치정보(trip));
        trip.add(point);
        loginUser = new LoginUser(member.id());
        postRepository.save(새로운_감상(point, member.id(), "", trip.id()));
    }

    @Test
    void 여행을_생성한다() {
        // given
        TripCreateResponse tripCreateResponse = tripService.create(loginUser);

        // expect
        assertThat(tripCreateResponse.tripId()).isNotNull();
    }

    @Test
    void 여행을_ID로_조회한다() {
        // when
        TripResponse tripResponse = tripService.readTripById(loginUser, trip.id());

        // expect
        assertThat(tripResponse).isEqualTo(TripResponse.from(trip));
    }

    @Test
    void 특정_회원의_전체_여행을_조회한다() {
        // when
        TripsSearchResponseOfMember tripsSearchResponseOfMember = tripService.readAllTripsOf(loginUser);

        // then
        assertThat(tripsSearchResponseOfMember)
                .usingRecursiveComparison()
                .isEqualTo(TripsSearchResponseOfMember.from(List.of(trip)));
    }

    @Test
    void 모든_회원의_감상이_있는_여행_전체를_조회한다() {
        // given
        TripSearchRequest request = TripSearchRequest.builder()
                .limit(10)
                .build();

        // when
        TripsSearchResponse tripsSearchResponse = tripService.readAll(request);

        // then
        assertThat(tripsSearchResponse)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(TripsSearchResponse.of(List.of(trip), false));
    }

    @Test
    void 여행의_이름과_상태를_수정한다() {
        // given
        TripUpdateRequest request = new TripUpdateRequest("제주도 여행", FINISHED);

        // when
        tripService.updateTripById(loginUser, trip.id(), request);

        // then
        Trip trip = tripRepository.getById(this.trip.id());
        assertSoftly(softly -> {
            softly.assertThat(trip.nameValue()).isEqualTo("제주도 여행");
            softly.assertThat(trip.status()).isEqualTo(FINISHED);
        });
    }

    @Test
    void 여행을_삭제한다() {
        // when
        tripService.delete(loginUser, trip.id());

        // then
        assertThatThrownBy(() -> tripService.readTripById(loginUser, trip.id()))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.message());
    }

    @Test
    void 여행을_삭제할_때_존재하지_않는_여행_ID이면_예외를_발생시킨다() {
        // expect
        assertThatThrownBy(() -> tripService.delete(new LoginUser(trip.id()), Long.MIN_VALUE))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.message());
    }

    @Test
    void 여행을_삭제할_때_존재하지_않는_사용자_닉네임이면_예외를_발생시킨다() {
        // given
        LoginUser invalidUser = new LoginUser(Long.MIN_VALUE);

        // expect
        assertThatThrownBy(() -> tripService.delete(invalidUser, trip.id()))
                .isInstanceOf(TripException.class)
                .hasMessage(NOT_AUTHORIZED_TO_TRIP.message());
    }

    @Test
    void 여행을_삭제할_때_로그인_한_사용자가_여행의_소유자가_아니면_예외가_발생한다() {
        // given
        Member otherMember = memberRepository.save(다른_사용자());
        LoginUser otherUser = new LoginUser(otherMember.id());

        // expect
        assertThatThrownBy(() -> tripService.delete(otherUser, trip.id()))
                .isInstanceOf(TripException.class)
                .hasMessage(NOT_AUTHORIZED_TO_TRIP.message());
    }
}
