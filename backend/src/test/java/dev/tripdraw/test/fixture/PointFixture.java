package dev.tripdraw.test.fixture;

import static dev.tripdraw.test.fixture.TripFixture.여행;

import dev.tripdraw.trip.domain.Point;
import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class PointFixture {

    public static Point 위치정보() {
        return new Point(1L, 1.1, 2.2, false, LocalDateTime.now(), 여행());
    }

    public static Point 위치정보(int year, int month, int dayOfMonth, int hour, int minute) {
        return new Point(1.1, 2.2, LocalDateTime.of(year, month, dayOfMonth, hour, minute));
    }
}
