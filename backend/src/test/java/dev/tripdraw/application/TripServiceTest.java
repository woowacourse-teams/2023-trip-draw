package dev.tripdraw.application;

import static dev.tripdraw.domain.trip.Status.ONGOING;
import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.dto.trip.PointCreateRequest;
import dev.tripdraw.dto.trip.PointResponse;
import dev.tripdraw.dto.trip.TripResponse;
import dev.tripdraw.exception.trip.TripException;
import java.time.LocalDateTime;
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
    void 여행에_위치_정보를_추가한다() {
        // given
        PointCreateRequest pointCreateRequest = new PointCreateRequest(trip.id(), 1.1, 2.2, LocalDateTime.now());

        // when
        PointResponse pointResponse = tripService.addPoint(loginUser, pointCreateRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(pointResponse.pointId()).isNotNull();
            softly.assertThat(pointResponse.latitude()).isEqualTo(pointCreateRequest.latitude());
            softly.assertThat(pointResponse.longitude()).isEqualTo(pointCreateRequest.longitude());
            softly.assertThat(pointResponse.recordedAt()).isEqualTo(pointCreateRequest.recordedAt());
        });
    }

    @Test
    void 여행에_위치_정보를_추가할_때_해당_여행이_존재하지_않으면_예외를_발생시킨다() {
        // given
        Long nonExistentId = Long.MIN_VALUE;
        PointCreateRequest pointCreateRequest = new PointCreateRequest(nonExistentId, 1.1, 2.2, LocalDateTime.now());

        // expect
        assertThatThrownBy(() -> tripService.addPoint(loginUser, pointCreateRequest))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.getMessage());
    }
}
