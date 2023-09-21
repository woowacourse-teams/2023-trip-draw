package dev.tripdraw.trip.domain;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.trip.exception.TripExceptionType.POINT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import dev.tripdraw.common.config.JpaConfig;
import dev.tripdraw.common.config.QueryDslConfig;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.trip.exception.TripException;
import java.time.LocalDateTime;
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

    private Trip trip;

    @BeforeEach
    void setUp() {
        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        trip = tripRepository.save(Trip.from(member));
    }

    @Test
    void 위치정보_ID로_위치정보를_조회한다() {
        // given
        Point point = new Point(3.14, 5.25, LocalDateTime.now());
        point.setTrip(trip);
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
}
