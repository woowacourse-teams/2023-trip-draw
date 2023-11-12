package dev.tripdraw.trip.domain;

import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.TripFixture.새로운_여행;
import static dev.tripdraw.trip.exception.TripExceptionType.TRIP_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import dev.tripdraw.common.config.JpaConfig;
import dev.tripdraw.common.config.QueryDslConfig;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.trip.exception.TripException;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({JpaConfig.class, QueryDslConfig.class})
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class TripRepositoryTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(사용자());
    }

    @Test
    void 회원_ID로_여행_목록을_조회한다() {
        // given
        Trip trip = tripRepository.save(새로운_여행(member));

        // when
        List<Trip> trips = tripRepository.findAllByMemberId(member.id());

        // then
        assertThat(trips).containsExactly(trip);
    }

    @Test
    void 회원_ID로_여행_목록을_조회할_때_해당_회원의_여행이_없다면_빈_여행_목록을_반환한다() {
        // when
        List<Trip> trips = tripRepository.findAllByMemberId(member.id());

        // then
        assertThat(trips).isEmpty();
    }

    @Test
    void 여행_ID로_여행을_조회한다() {
        // given
        Trip trip = tripRepository.save(새로운_여행(member));

        // when
        Trip foundTrip = tripRepository.getByTripId(trip.id());

        // then
        assertThat(foundTrip).isEqualTo(trip);
    }

    @Test
    void 여행_ID로_여행을_조회할_때_여행이_존재하지_않는_경우_예외를_발생시킨다() {
        // given
        Long invalidId = Long.MIN_VALUE;

        // expect
        assertThatThrownBy(() -> tripRepository.getByTripId(invalidId))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.message());
    }

    @Test
    void 여행_ID로_위치정보_목록이_포함된_여행을_조회한다() {
        // given
        Trip trip = 새로운_여행(member);
        Point point1 = 새로운_위치정보(trip);
        Point point2 = 새로운_위치정보(trip);
        trip.add(point1);
        trip.add(point2);
        tripRepository.save(trip);

        // when
        Trip savedTrips = tripRepository.getTripWithPoints(trip.id());

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedTrips.points()).containsExactly(point1, point2);
            softly.assertThat(savedTrips).isEqualTo(trip);
        });
    }

    @Test
    void 여행_ID로_위치정보_목록이_포함된_여행을_조회할_때_여행이_존재하지_않는_경우_예외를_발생시킨다() {
        // given
        Long invalidId = Long.MIN_VALUE;

        // expect
        assertThatThrownBy(() -> tripRepository.getTripWithPoints(invalidId))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.message());
    }

    @Test
    void 여행_ID로_위치정보_목록과_회원이_포함된_여행을_조회한다() {
        // given
        Trip trip = 새로운_여행(member);
        Point point1 = 새로운_위치정보(trip);
        Point point2 = 새로운_위치정보(trip);
        trip.add(point1);
        trip.add(point2);
        tripRepository.save(trip);

        // when
        Trip savedTrips = tripRepository.getTripWithPointsAndMemberByTripId(trip.id());

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedTrips.points()).containsExactly(point1, point2);
            softly.assertThat(savedTrips.member()).isEqualTo(member);
            softly.assertThat(savedTrips).isEqualTo(trip);
        });
    }

    @Test
    void 여행_ID로_위치정보_목록과_회원이_포함된_여행을_조회할_때_여행이_존재하지_않는_경우_예외를_발생시킨다() {
        // given
        Long invalidId = Long.MIN_VALUE;

        // expect
        assertThatThrownBy(() -> tripRepository.getTripWithPointsAndMemberByTripId(invalidId))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.message());
    }

    @Test
    void 회원_ID로_여행을_삭제한다() {
        // given
        Trip trip = tripRepository.save(새로운_여행(member));

        // when
        tripRepository.deleteByMemberId(member.id());

        // then
        assertThat(tripRepository.existsById(trip.id())).isFalse();
    }

    @Test
    void 회원_ID를_갖는_모든_여행_ID를_조회한다() {
        // given
        List<Long> tripIds = IntStream.range(0, 5)
                .mapToObj(value -> tripRepository.save(새로운_여행(member)).id())
                .toList();

        // when
        List<Long> foundIds = tripRepository.findAllTripIdsByMemberId(member.id());

        // then
        assertThat(foundIds).isEqualTo(tripIds);
    }
}
