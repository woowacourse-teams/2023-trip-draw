package dev.tripdraw.domain.trip;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TripDurationTest {

    private final TripDuration tripDuration = TripDuration.of(Duration.ofMinutes(3248));

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
