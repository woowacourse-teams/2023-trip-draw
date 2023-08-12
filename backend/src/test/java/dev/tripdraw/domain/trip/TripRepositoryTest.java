package dev.tripdraw.domain.trip;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_NOT_FOUND;
import static java.lang.Long.MIN_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.exception.trip.TripException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
class TripRepositoryTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
    }

    @Test
    void 회원_ID로_여행_목록을_조회한다() {
        // given
        Trip trip = new Trip(TripName.from("제주도 여행"), member);
        tripRepository.save(trip);

        // when
        List<Trip> trips = tripRepository.findAllByMemberId(member.id());

        // then
        assertSoftly(softly -> {
            softly.assertThat(trips).hasSize(1);
            softly.assertThat(trips.get(0)).isEqualTo(trip);
        });
    }

    @Test
    void 회원_ID로_여행_목록을_조회할_때_해당_회원의_여행이_없다면_빈_여행_목록을_반환한다() {
        // given & when
        List<Trip> trips = tripRepository.findAllByMemberId(member.id());

        // then
        assertThat(trips).hasSize(0);
    }

    @Test
    void 여행_ID를_입력받아_여행_목록과_위치_정보까지_조회한다() {
        // given
        Trip trip = new Trip(TripName.from("제주도 여행"), member);
        Point point1 = new Point(1.1, 2.2, LocalDateTime.now());
        Point point2 = new Point(3.3, 4.4, LocalDateTime.now());
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
    void 회원_ID로_여행을_삭제한다() {
        // given
        Trip trip = new Trip(TripName.from("제주도 여행"), member);
        tripRepository.save(trip);

        // when
        tripRepository.deleteByMemberId(member.id());

        // then
        assertThat(tripRepository.findById(trip.id())).isEmpty();
    }

    @Test
    void 여행_ID로_여행을_조회한다() {
        // given
        Trip trip = tripRepository.save(new Trip(TripName.from("제주도 여행"), member));

        // when
        Trip foundTrip = tripRepository.getById(trip.id());

        // then
        assertThat(foundTrip).isEqualTo(trip);
    }

    @Test
    void 여행_ID로_여행을_조회할_때_존재하지_않는_경우_예외를_발생시킨다() {
        // given
        Long wrongId = MIN_VALUE;

        // expect
        assertThatThrownBy(() -> tripRepository.getById(wrongId))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_NOT_FOUND.message());
    }
}
