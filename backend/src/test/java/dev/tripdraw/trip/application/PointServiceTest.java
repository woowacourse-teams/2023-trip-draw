package dev.tripdraw.trip.application;

import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.TripFixture.새로운_여행;
import static dev.tripdraw.trip.application.PointServiceTest.PostRequestFixture.위치정보_생성_요청;
import static dev.tripdraw.trip.exception.TripExceptionType.NOT_AUTHORIZED_TO_TRIP;
import static dev.tripdraw.trip.exception.TripExceptionType.TRIP_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.dto.PointCreateRequest;
import dev.tripdraw.trip.dto.PointCreateResponse;
import dev.tripdraw.trip.dto.PointResponse;
import dev.tripdraw.trip.exception.TripException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
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
class PointServiceTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PointRepository pointRepository;

    @MockBean
    private RouteImageGenerator routeImageGenerator;

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
    }

    @Test
    void 여행에_위치정보를_추가한다() {
        // given
        PointCreateRequest pointCreateRequest = 위치정보_생성_요청(trip.id());

        // when
        PointCreateResponse response = pointService.create(loginUser, pointCreateRequest);

        // then
        Trip savedTrip = tripRepository.getById(trip.id());
        assertThat(savedTrip.points())
                .extracting(Point::id)
                .contains(response.pointId());
    }

    @Test
    void 여행에_위치정보를_추가할_때_해당_여행이_존재하지_않으면_예외를_발생시킨다() {
        // given
        Long invalidTripId = Long.MIN_VALUE;
        PointCreateRequest pointCreateRequest = 위치정보_생성_요청(invalidTripId);

        // expect
        assertThatThrownBy(() -> pointService.create(loginUser, pointCreateRequest))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.message());
    }

    @Test
    void 여행에서_위치정보를_삭제한다() {
        // when
        pointService.delete(loginUser, point.id(), trip.id());

        // then
        assertThat(pointRepository.findById(point.id())).isEmpty();
    }

    @Test
    void 여행에서_위치정보를_삭제시_인가에_실패하면_예외를_발생시킨다() {
        // given
        LoginUser otherUser = new LoginUser(Long.MIN_VALUE);

        // expect
        assertThatThrownBy(() -> pointService.delete(otherUser, point.id(), trip.id()))
                .isInstanceOf(TripException.class)
                .hasMessage(NOT_AUTHORIZED_TO_TRIP.message());
    }

    @Test
    void 위치_정보를_조회한다() {
        // when
        PointResponse pointResponse = pointService.read(loginUser, trip.id(), point.id());

        // then
        assertThat(pointResponse)
                .usingRecursiveComparison()
                .isEqualTo(PointResponse.from(point));
    }

    static class PostRequestFixture {
        public static PointCreateRequest 위치정보_생성_요청(Long tripId) {
            return new PointCreateRequest(tripId, 1.1, 2.2, LocalDateTime.now());
        }
    }
}
