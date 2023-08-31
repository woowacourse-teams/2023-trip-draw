package dev.tripdraw.trip.application;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static dev.tripdraw.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.trip.domain.TripStatus.FINISHED;
import static dev.tripdraw.trip.exception.TripExceptionType.POINT_ALREADY_DELETED;
import static dev.tripdraw.trip.exception.TripExceptionType.POINT_NOT_IN_TRIP;
import static dev.tripdraw.trip.exception.TripExceptionType.TRIP_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.application.ServiceTest;
import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.member.exception.MemberException;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.dto.PointCreateRequest;
import dev.tripdraw.trip.dto.PointCreateResponse;
import dev.tripdraw.trip.dto.PointResponse;
import dev.tripdraw.trip.dto.TripCreateResponse;
import dev.tripdraw.trip.dto.TripResponse;
import dev.tripdraw.trip.dto.TripSearchResponse;
import dev.tripdraw.trip.dto.TripUpdateRequest;
import dev.tripdraw.trip.dto.TripsSearchResponse;
import dev.tripdraw.trip.exception.TripException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@ServiceTest
class TripServiceTest {

    @Autowired
    private TripService tripService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private RouteImageGenerator routeImageGenerator;

    private Trip trip;
    private LoginUser loginUser;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        trip = tripRepository.save(Trip.from(member));
        loginUser = new LoginUser(member.id());
    }

    @Test
    void 여행을_생성한다() {
        // given
        TripCreateResponse tripCreateResponse = tripService.create(loginUser);

        // expect
        assertThat(tripCreateResponse.tripId()).isNotNull();
    }

    @Test
    void 여행에_위치정보를_추가한다() {
        // given
        PointCreateRequest pointCreateRequest = new PointCreateRequest(trip.id(), 1.1, 2.2, LocalDateTime.now());

        // when
        PointCreateResponse response = tripService.addPoint(loginUser, pointCreateRequest);

        // then
        assertThat(response.pointId()).isNotNull();
    }

    @Test
    void 여행에_위치정보를_추가할_때_해당_여행이_존재하지_않으면_예외를_발생시킨다() {
        // given
        Long nonExistentId = Long.MIN_VALUE;
        PointCreateRequest pointCreateRequest = new PointCreateRequest(nonExistentId, 1.1, 2.2, LocalDateTime.now());

        // expect
        assertThatThrownBy(() -> tripService.addPoint(loginUser, pointCreateRequest))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.message());
    }

    @Test
    void 여행을_ID로_조회한다() {
        // given & when
        TripResponse tripResponse = tripService.readTripById(loginUser, trip.id());

        // then
        assertSoftly(softly -> {
            softly.assertThat(tripResponse.tripId()).isNotNull();
            softly.assertThat(tripResponse.name()).isNotNull();
            softly.assertThat(tripResponse.route()).isNotNull();
            softly.assertThat(tripResponse.status()).isNotNull();
        });
    }

    @Test
    void 여행에서_위치정보를_삭제한다() {
        // given
        PointCreateRequest pointCreateRequest = new PointCreateRequest(trip.id(), 1.1, 2.2, LocalDateTime.now());
        PointCreateResponse response = tripService.addPoint(loginUser, pointCreateRequest);

        // when
        tripService.deletePoint(loginUser, response.pointId(), trip.id());

        // then
        Point deletedPoint = trip.route().points()
                .stream()
                .filter(point -> Objects.equals(point.id(), response.pointId()))
                .findFirst()
                .get();

        assertThat(deletedPoint.isDeleted()).isTrue();
    }

    @Test
    void 여행에서_위치정보를_삭제시_인가에_실패하면_예외를_발생시킨다() {
        // given
        PointCreateRequest pointCreateRequest = new PointCreateRequest(trip.id(), 1.1, 2.2, LocalDateTime.now());
        PointCreateResponse response = tripService.addPoint(loginUser, pointCreateRequest);
        LoginUser otherUser = new LoginUser(Long.MIN_VALUE);

        // expect
        assertThatThrownBy(() -> tripService.deletePoint(otherUser, response.pointId(), trip.id()))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void 여행에서_위치정보를_삭제시_여행에_해당_위치정보가_존재하지_않으면_예외를_발생시킨다() {
        // given
        PointCreateRequest pointCreateRequest = new PointCreateRequest(trip.id(), 1.1, 2.2, LocalDateTime.now());
        tripService.addPoint(loginUser, pointCreateRequest);

        Point inExistentPoint = new Point(Long.MAX_VALUE, 1.1, 2.2, false, LocalDateTime.now());

        // expect
        assertThatThrownBy(() -> tripService.deletePoint(loginUser, inExistentPoint.id(), trip.id()))
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_NOT_IN_TRIP.message());
    }

    @Test
    void 여행에서_위치정보를_삭제시_이미_삭제된_위치정보면_예외를_발생시킨다() {
        // given
        PointCreateRequest pointCreateRequest = new PointCreateRequest(trip.id(), 1.1, 2.2, LocalDateTime.now());
        PointCreateResponse response = tripService.addPoint(loginUser, pointCreateRequest);
        tripService.deletePoint(loginUser, response.pointId(), trip.id());

        // expect
        assertThatThrownBy(() -> tripService.deletePoint(loginUser, response.pointId(), trip.id()))
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_ALREADY_DELETED.message());
    }

    @Test
    void 전체_여행을_조회한다() {
        // given & when
        TripsSearchResponse tripsSearchResponse = tripService.readAllTrips(loginUser);

        // then
        assertThat(tripsSearchResponse).usingRecursiveComparison().isEqualTo(
                new TripsSearchResponse(List.of(
                        new TripSearchResponse(
                                trip.id(),
                                trip.nameValue(),
                                trip.imageUrl(),
                                trip.routeImageUrl()
                        )
                ))
        );
    }

    @Test
    void 여행의_이름과_상태를_수정한다() {
        // given
        TripUpdateRequest request = new TripUpdateRequest("제주도 여행", FINISHED);

        // when
        tripService.updateTripById(loginUser, trip.id(), request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(trip.nameValue()).isEqualTo("제주도 여행");
            softly.assertThat(trip.status()).isEqualTo(FINISHED);
        });
    }

    @Test
    void 위치_정보를_조회한다() {
        // given
        PointCreateRequest pointCreateRequest = new PointCreateRequest(
                trip.id(),
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );
        Long pointId = tripService.addPoint(loginUser, pointCreateRequest).pointId();

        // when
        PointResponse pointResponse = tripService.readPointByTripAndPointId(loginUser, trip.id(), pointId);

        // then
        assertThat(pointResponse).usingRecursiveComparison().isEqualTo(
                new PointResponse(
                        pointId,
                        1.1,
                        2.2,
                        false,
                        LocalDateTime.of(2023, 7, 18, 20, 24)
                )
        );
    }
}
