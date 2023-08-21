package dev.tripdraw.domain.trip;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TripDurationTest {

    private final LocalDateTime localDateTime = LocalDateTime.of(2023, 8, 3, 4, 0);
    private final Point startingPoint = new Point(1.1, 1.1, localDateTime);
    private final Point arrivalPoint = new Point(1.1, 1.1, localDateTime.plusMinutes(3248));
    private final TripDuration tripDuration = TripDuration.of(startingPoint, arrivalPoint);

    @Test
    void 여행기간을_분_단위로_나타낸다() {
        // given & when
        String durationInMinutes = tripDuration.durationInMinutes();

        // then
        assertThat(durationInMinutes).isEqualTo("3248분");
    }

    @Test
    void 여행기간을_시간과_분_단위로_나타낸다() {
        // given & when
        String durationInHoursAndMinutes = tripDuration.durationInHoursAndMinutes();

        // then
        assertThat(durationInHoursAndMinutes).isEqualTo("54시간 8분");
    }

    @Test
    void 여행기간을_일과_시간과_분_단위로_나타낸다() {
        // given & when
        String durationInDaysAndHoursAndMinutes = tripDuration.durationInDaysAndHoursAndMinutes();

        // then
        assertThat(durationInDaysAndHoursAndMinutes).isEqualTo("2일 6시간 8분");
    }
}
