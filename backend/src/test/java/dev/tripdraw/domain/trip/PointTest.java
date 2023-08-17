package dev.tripdraw.domain.trip;

import static dev.tripdraw.exception.trip.TripExceptionType.POINT_ALREADY_DELETED;
import static dev.tripdraw.exception.trip.TripExceptionType.POINT_ALREADY_HAS_POST;
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
                .hasMessage(POINT_ALREADY_DELETED.message());
    }

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
}
