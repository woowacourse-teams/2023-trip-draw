package dev.tripdraw.trip.domain;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.trip.exception.TripExceptionType.POINT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import dev.tripdraw.common.config.JpaConfig;
import dev.tripdraw.common.config.QueryDslConfig;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.trip.exception.TripException;
import java.time.LocalDateTime;
import java.util.List;
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
class PointRepositoryTest {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Trip huchuTrip;
    private Trip herbTrip;

    @BeforeEach
    void setUp() {
        Member huchu = memberRepository.save(new Member("후추", "kakaoId", KAKAO));
        Member herb = memberRepository.save(new Member("허브", "kakaoId", KAKAO));
        huchuTrip = tripRepository.save(Trip.from(huchu));
        herbTrip = tripRepository.save(Trip.from(herb));
    }

    @Test
    void 위치정보_ID로_위치정보를_조회한다() {
        // given
        Point point = new Point(3.14, 5.25, LocalDateTime.now());
        point.setTrip(huchuTrip);
        pointRepository.save(point);

        // when
        Point foundPoint = pointRepository.getById(point.id());

        // then
        assertThat(foundPoint).isEqualTo(point);
    }

    @Test
    void 위치정보_ID로_위치정보를_조회할_때_존재하지_않는_경우_예외를_발생시킨다() {
        // given
        Long wrongId = Long.MIN_VALUE;

        // expect
        assertThatThrownBy(() -> pointRepository.getById(wrongId))
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_NOT_FOUND.message());
    }

    @Test
    void 여행_ID_목록으로_위치정보를_삭제한다() {
        // given
        Point huchuFirstPoint = new Point(3.14, 5.25, LocalDateTime.now());
        huchuFirstPoint.setTrip(huchuTrip);
        Point huchuSecondPoint = new Point(3.14, 5.25, LocalDateTime.now());
        huchuSecondPoint.setTrip(huchuTrip);

        Point herbFirstPoint = new Point(3.14, 5.25, LocalDateTime.now());
        herbFirstPoint.setTrip(herbTrip);
        Point herbSecondPoint = new Point(3.14, 5.25, LocalDateTime.now());
        herbSecondPoint.setTrip(herbTrip);

        pointRepository.save(huchuFirstPoint);
        pointRepository.save(huchuSecondPoint);
        pointRepository.save(herbFirstPoint);
        pointRepository.save(herbSecondPoint);

        List<Long> tripIds = List.of(huchuTrip.id(), herbTrip.id());

        // when
        pointRepository.deleteByTripIds(tripIds);
        
        // then
        assertSoftly(softly -> {
            softly.assertThat(pointRepository.existsById(huchuFirstPoint.id())).isFalse();
            softly.assertThat(pointRepository.existsById(huchuSecondPoint.id())).isFalse();
            softly.assertThat(pointRepository.existsById(herbFirstPoint.id())).isFalse();
            softly.assertThat(pointRepository.existsById(herbSecondPoint.id())).isFalse();
        });
    }
}
