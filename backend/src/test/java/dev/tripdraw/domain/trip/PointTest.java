package dev.tripdraw.domain.trip;

import static dev.tripdraw.exception.trip.TripExceptionType.POINT_ALREADY_DELETED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.exception.trip.TripException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PointTest {

    @Test
    void 위치정보를_삭제한다() {
        // given
        Point point = new Point();

        // when
        point.delete();

        // then
        assertThat(point.isDeleted()).isTrue();
    }

    @Test
    void 이미_삭제된_위치정보를_삭제하면_예외를_발생시킨다() {
        // given
        Point point = new Point();
        point.delete();

        // expect
        assertThatThrownBy(point::delete)
                .isInstanceOf(TripException.class)
                .hasMessage(POINT_ALREADY_DELETED.getMessage());
    }
}
