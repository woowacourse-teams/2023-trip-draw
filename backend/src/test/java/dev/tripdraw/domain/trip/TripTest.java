package dev.tripdraw.domain.trip;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.exception.ForbiddenException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class TripTest {

    @Test
    void 여행_경로에_좌표를_추가한다() {
        // given
        Member member = new Member("tonghuchu");
        Trip trip = new Trip("trip", member);
        Point point = new Point(1.1, 2.2, LocalDateTime.now());

        // when
        trip.add(point);

        // then
        Route route = trip.getRoute();
        assertThat(route.getPoints()).hasSize(1);
    }

    @Test
    void 인가된_사용자는_예외가_발생하지_않는다() {
        // given
        Member member = new Member("tonghuchu");
        Trip trip = new Trip("trip", member);

        // expect
        assertThatNoException().isThrownBy(() -> trip.validateAuthorization(member));
    }

    @Test
    void 인가되지_않은_사용자는_예외가_발생한다() {
        // given
        Member member = new Member("tonghuchu");
        Member other = new Member("other");
        Trip trip = new Trip("trip", member);

        // expect
        assertThatThrownBy(() -> trip.validateAuthorization(other))
                .isInstanceOf(ForbiddenException.class);
    }
}
