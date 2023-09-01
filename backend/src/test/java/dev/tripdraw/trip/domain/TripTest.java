package dev.tripdraw.trip.domain;

import static dev.tripdraw.auth.domain.OauthType.KAKAO;
import static dev.tripdraw.trip.exception.TripExceptionType.NOT_AUTHORIZED_TO_TRIP;
import static dev.tripdraw.trip.exception.TripExceptionType.TRIP_INVALID_STATUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.member.domain.Member;
import dev.tripdraw.trip.exception.TripException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class TripTest {

    @Test
    void 여행_경로에_좌표를_추가한다() {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);
        Trip trip = Trip.from(member);
        Point point = new Point(1.1, 2.2, LocalDateTime.now());

        // when
        trip.add(point);

        // then
        Route route = trip.route();
        assertSoftly(softly -> {
            softly.assertThat(route.points()).hasSize(1);
            softly.assertThat(point.trip()).isEqualTo(trip);
        });
    }

    @Nested
    class 위치정보_포함_여부를_확인할_때 {

        @Test
        void 위치정보를_포함하는_여행이면_참값을_반환한다() {
            // given
            Member member = new Member("통후추", "kakaoId", KAKAO);
            Trip trip = Trip.from(member);
            Point point = new Point(1.1, 2.2, LocalDateTime.now());
            trip.add(point);

            // expect
            assertThat(trip.contains(point)).isTrue();
        }

        @Test
        void 위치정보를_포함하지_않는_여행이면_참값을_반환하지_않는다() {
            // given
            Member member = new Member("통후추", "kakaoId", KAKAO);
            Trip trip = Trip.from(member);
            Point point = new Point(1.1, 2.2, LocalDateTime.now());

            // expect
            assertThat(trip.contains(point)).isFalse();
        }
    }

    @Test
    void 인가된_사용자는_예외가_발생하지_않는다() {
        // given
        Member member = new Member("tonghuchu", "kakaoId", KAKAO);
        Trip trip = Trip.from(member);

        // expect
        assertThatNoException().isThrownBy(() -> trip.validateAuthorization(member));
    }

    @Test
    void 인가되지_않은_사용자는_예외가_발생한다() {
        // given
        Member member = new Member("tonghuchu", "kakaoId", KAKAO);
        Trip trip = Trip.from(member);

        // expect
        assertThatThrownBy(() -> trip.validateAuthorization(new Member("other", "kakaoId", KAKAO)))
                .isInstanceOf(TripException.class)
                .hasMessage(NOT_AUTHORIZED_TO_TRIP.message());
    }

    @Test
    void 이름을_반환한다() {
        // given
        Member member = new Member("tonghuchu", "kakaoId", KAKAO);
        Trip trip = new Trip(TripName.from("통후추"), member);

        // expect
        assertThat(trip.nameValue()).isEqualTo("통후추의 여행");
    }

    @Test
    void 이름을_변경한다() {
        // given
        Member member = new Member("tonghuchu", "kakaoId", KAKAO);
        Trip trip = new Trip(TripName.from("통후추"), member);

        // when
        trip.changeName("제주도 여행");

        // then
        assertThat(trip.nameValue()).isEqualTo("제주도 여행");
    }

    @Test
    void 경로에_해당하는_모든_위치를_반환한다() {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);
        Trip trip = Trip.from(member);
        Point point1 = new Point(1.1, 2.2, LocalDateTime.now());
        Point point2 = new Point(3.3, 4.4, LocalDateTime.now());
        trip.add(point1);
        trip.add(point2);

        // when
        List<Point> result = trip.points();

        // then
        assertThat(result).containsExactly(point1, point2);
    }

    @ParameterizedTest
    @CsvSource({"ONGOING, ONGOING", "FINISHED, FINISHED"})
    void 여행_상태를_변경한다(TripStatus target, TripStatus expected) {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);
        Trip trip = Trip.from(member);

        // when
        trip.changeStatus(target);

        // then
        assertThat(trip.status()).isEqualTo(expected);
    }

    @Test
    void 여행_상태를_null로_변경하려할_경우_예외를_발생시킨다() {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);
        Trip trip = Trip.from(member);

        // expect
        assertThatThrownBy(() -> trip.changeStatus(null))
                .isInstanceOf(TripException.class)
                .hasMessage(TRIP_INVALID_STATUS.message());
    }

    @Test
    void 감상_사진_URL을_변경한다() {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);
        Trip trip = Trip.from(member);

        // when
        trip.changeImageUrl("/통후추셀카.jpg");

        // then
        assertThat(trip.imageUrl()).isEqualTo("/통후추셀카.jpg");
    }

    @Test
    void 경로_이미지_URL을_변경한다() {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);
        Trip trip = Trip.from(member);

        // when
        trip.changeRouteImageUrl("/통후추여행경로.png");

        // then
        assertThat(trip.routeImageUrl()).isEqualTo("/통후추여행경로.png");
    }

    @Test
    void 위도를_반환한다() {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);
        Trip trip = Trip.from(member);
        trip.add(new Point(1.1, 2.2, LocalDateTime.now()));
        trip.add(new Point(3.3, 4.4, LocalDateTime.now()));
        trip.add(new Point(5.5, 6.6, LocalDateTime.now()));

        // when
        List<Double> latitudes = trip.getLatitudes();

        // then
        assertThat(latitudes).containsExactly(1.1, 3.3, 5.5);
    }

    @Test
    void 경도를_반환한다() {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);
        Trip trip = Trip.from(member);
        trip.add(new Point(1.1, 2.2, LocalDateTime.now()));
        trip.add(new Point(3.3, 4.4, LocalDateTime.now()));
        trip.add(new Point(5.5, 6.6, LocalDateTime.now()));

        // when
        List<Double> longitudes = trip.getLongitudes();

        // then
        assertThat(longitudes).containsExactly(2.2, 4.4, 6.6);
    }

    @Test
    void 감상을_남긴_위치_정보의_위도를_반환한다() {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);
        Trip trip = Trip.from(member);
        trip.add(new Point(1.1, 2.2, true, LocalDateTime.now()));
        trip.add(new Point(3.3, 4.4, false, LocalDateTime.now()));
        trip.add(new Point(5.5, 6.6, true, LocalDateTime.now()));

        // when
        List<Double> pointedLatitudes = trip.getPointedLatitudes();

        // then
        assertThat(pointedLatitudes).containsExactly(1.1, 5.5);
    }

    @Test
    void 감상을_남긴_위치_정보의_경도를_반환한다() {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);
        Trip trip = Trip.from(member);
        trip.add(new Point(1.1, 2.2, true, LocalDateTime.now()));
        trip.add(new Point(3.3, 4.4, false, LocalDateTime.now()));
        trip.add(new Point(5.5, 6.6, true, LocalDateTime.now()));

        // when
        List<Double> pointedLongitudes = trip.getPointedLongitudes();

        // then
        assertThat(pointedLongitudes).containsExactly(2.2, 6.6);
    }
}
