package dev.tripdraw.test.fixture;

import static dev.tripdraw.test.fixture.MemberFixture.사용자;

import dev.tripdraw.member.domain.Member;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripName;
import dev.tripdraw.trip.domain.TripStatus;

@SuppressWarnings("NonAsciiCharacters")
public class TripFixture {

    public static Trip 여행() {
        return new Trip(1L, TripName.from("통후추"), 사용자().id(), TripStatus.ONGOING, "", "");
    }

    public static Trip 새로운_여행(Member member) {
        return new Trip(TripName.from(member.nickname()), member.id());
    }
}
