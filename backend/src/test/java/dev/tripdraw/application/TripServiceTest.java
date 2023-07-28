package dev.tripdraw.application;

import static dev.tripdraw.domain.trip.TripStatus.ONGOING;
import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.exception.trip.TripExceptionType.POINT_ALREADY_DELETED;
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
import dev.tripdraw.dto.trip.PointCreateRequest;
import dev.tripdraw.dto.trip.PointCreateResponse;
import dev.tripdraw.dto.trip.PointDeleteRequest;
import dev.tripdraw.dto.trip.TripResponse;
import dev.tripdraw.exception.member.MemberException;
import dev.tripdraw.exception.trip.TripException;
import java.time.LocalDateTime;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TripServiceTest {

    @Autowired
    private TripService tripService;

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

    @Test
    void 여행을_생성한다() {
        // given
        TripResponse tripResponse = tripService.create(loginUser);

        // expect
        assertSoftly(softly -> {
            softly.assertThat(tripResponse.tripId()).isNotNull();
            softly.assertThat(tripResponse.name()).isNotNull();
            softly.assertThat(tripResponse.routes()).isEmpty();
            softly.assertThat(tripResponse.status()).isEqualTo(ONGOING);
        });
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
                .hasMessage(TRIP_NOT_FOUND.getMessage());
    }

    @Test
    void 여행을_ID로_조회한다() {
        // given & when
        TripResponse tripResponse = tripService.readTripById(loginUser, trip.id());

        // then
        assertSoftly(softly -> {
            softly.assertThat(tripResponse.tripId()).isNotNull();
            softly.assertThat(tripResponse.name()).isNotNull();
            softly.assertThat(tripResponse.routes()).isNotNull();
            softly.assertThat(tripResponse.status()).isNotNull();
        });
    }

    @Test
    void 여행에서_위치정보를_삭제한다() {
        // given
        PointCreateRequest pointCreateRequest = new PointCreateRequest(trip.id(), 1.1, 2.2, LocalDateTime.now());
        PointCreateResponse response = tripService.addPoint(loginUser, pointCreateRequest);
        PointDeleteRequest pointDeleteRequest = new PointDeleteRequest(trip.id(), response.pointId());

        // when
        tripService.deletePoint(loginUser, pointDeleteRequest);

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
        PointDeleteRequest pointDeleteRequest = new PointDeleteRequest(trip.id(), response.pointId());
        LoginUser otherUser = new LoginUser("순후추");

        // expect
        assertThatThrownBy(() -> tripService.deletePoint(otherUser, pointDeleteRequest))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 여행에서_위치정보를_삭제시_여행에_해당_위치정보가_존재하지_않으면_예외를_발생시킨다() {
        // given
        PointCreateRequest pointCreateRequest = new PointCreateRequest(trip.id(), 1.1, 2.2, LocalDateTime.now());
        tripService.addPoint(loginUser, pointCreateRequest);

        Point inExistentPoint = new Point(Long.MAX_VALUE, 1.1, 2.2, LocalDateTime.now());
        PointDeleteRequest pointDeleteRequest = new PointDeleteRequest(trip.id(), inExistentPoint.id());

        // expect
        assertThatThrownBy(() -> tripService.deletePoint(loginUser, pointDeleteRequest))
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_NOT_FOUND.getMessage());
    }

    @Test
    void 여행에서_위치정보를_삭제시_이미_삭제된_위치정보면_예외를_발생시킨다() {
        // given
        PointCreateRequest pointCreateRequest = new PointCreateRequest(trip.id(), 1.1, 2.2, LocalDateTime.now());
        PointCreateResponse response = tripService.addPoint(loginUser, pointCreateRequest);
        PointDeleteRequest pointDeleteRequest = new PointDeleteRequest(trip.id(), response.pointId());
        tripService.deletePoint(loginUser, pointDeleteRequest);

        // expect
        assertThatThrownBy(() -> tripService.deletePoint(loginUser, pointDeleteRequest))
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_ALREADY_DELETED.getMessage());
    }
}
