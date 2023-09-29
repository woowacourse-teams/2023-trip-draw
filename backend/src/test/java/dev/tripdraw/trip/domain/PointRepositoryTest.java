package dev.tripdraw.trip.domain;

import static dev.tripdraw.test.fixture.MemberFixture.새로운_사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.TripFixture.새로운_여행;
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
        Member huchu = memberRepository.save(새로운_사용자("후추"));
        Member herb = memberRepository.save(새로운_사용자("허브"));
        huchuTrip = tripRepository.save(새로운_여행(huchu));
        herbTrip = tripRepository.save(새로운_여행(herb));
    }

    @Test
    void 위치정보_ID로_위치정보를_조회한다() {
        // given
        Point point = 새로운_위치정보(huchuTrip);
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
        Long invalidId = Long.MIN_VALUE;

        // expect
        assertThatThrownBy(() -> pointRepository.getById(invalidId))
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_NOT_FOUND.message());
    }

    @Test
    void 여행_ID_목록으로_위치정보를_삭제한다() {
        // given
        Point huchuPoint1 = 새로운_위치정보(huchuTrip);
        Point huchuPoint2 = 새로운_위치정보(huchuTrip);
        huchuPoint1.setTrip(huchuTrip);
        huchuPoint2.setTrip(huchuTrip);
        Point herbPoint = 새로운_위치정보(herbTrip);
        herbPoint.setTrip(herbTrip);

        pointRepository.saveAll(List.of(huchuPoint1, huchuPoint2, herbPoint));
        List<Long> tripIds = List.of(huchuTrip.id(), herbTrip.id());

        // when
        pointRepository.deleteByTripIds(tripIds);

        // then
        assertSoftly(softly -> {
            softly.assertThat(pointRepository.existsById(huchuPoint1.id())).isFalse();
            softly.assertThat(pointRepository.existsById(huchuPoint2.id())).isFalse();
            softly.assertThat(pointRepository.existsById(herbPoint.id())).isFalse();
        });
    }
}
