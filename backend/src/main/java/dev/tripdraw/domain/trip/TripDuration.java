package dev.tripdraw.domain.trip;

import java.time.Duration;

public class TripDuration {

    private static final String MINUTE = "분";
    private static final String HOUR = "시간";
    private static final String DAY = "일";
    private static final String WHITE_SPACE = " ";

    private final Duration duration;

    private TripDuration(Duration duration) {
        this.duration = duration;
    }

    public static TripDuration of(Duration duration) {
        return new TripDuration(duration);
    }

    public String durationInMinutes() {
        long minutes = duration.toMinutes();
        return minutes + MINUTE;
    }

    public String durationInHoursAndMinutes() {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() - (hours * 60);

        return hours + HOUR + WHITE_SPACE + minutes + MINUTE;
    }

    public String durationInDaysAndHoursAndMinutes() {
        long days = duration.toDays();
        long hours = duration.toHours() - (days * 24);
        long minutes = duration.toMinutes() - (days * 1440) - (hours * 60);

        return days + DAY + WHITE_SPACE + hours + HOUR + WHITE_SPACE + minutes + MINUTE;
    }
}
