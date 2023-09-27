package dev.tripdraw.test.fixture;

import static dev.tripdraw.test.fixture.MemberFixture.사용자;

import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripName;
import dev.tripdraw.trip.domain.TripStatus;

@SuppressWarnings("NonAsciiCharacters")
public class TripFixture {

    public static Trip 여행() {
        return new Trip(1L, TripName.from("통후추"), 사용자().id(), TripStatus.ONGOING, "", "");
    }
}
