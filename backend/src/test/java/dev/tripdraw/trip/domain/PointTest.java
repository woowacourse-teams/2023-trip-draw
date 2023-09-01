package dev.tripdraw.trip.domain;

import static dev.tripdraw.auth.domain.OauthType.KAKAO;
import static dev.tripdraw.trip.exception.TripExceptionType.POINT_ALREADY_HAS_POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.member.domain.Member;
import dev.tripdraw.trip.exception.TripException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PointTest {

    @Test
    void 위치에_감상을_등록한다() {
        // given
        Point point = new Point();

        // when
        point.registerPost();

        // then
        assertThat(point.hasPost()).isTrue();
    }

    @Test
    void 위치에_감상을_등록할_때_이미_감상이_등록되어_있으면_예외가_발생한다() {
        // given
        Point point = new Point();
        point.registerPost();

        // expect
        assertThatThrownBy(point::registerPost)
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_ALREADY_HAS_POST.message());
    }

    @Test
    void 여행을_등록한다() {
        // given
        Point point = new Point(3.14, 5.25, LocalDateTime.now());
        Member member = new Member("통후추", "kakaoId", KAKAO);
        Trip trip = Trip.from(member);

        // when
        point.setTrip(trip);

        // then
        assertSoftly(softly -> {
            softly.assertThat(point.trip()).isEqualTo(trip);
            softly.assertThat(trip.route().points()).contains(point);
        });
    }
}
