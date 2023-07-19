package dev.tripdraw.domain.trip;

import dev.tripdraw.domain.member.Member;
import jakarta.persistence.Embeddable;

@Embeddable
public class TripName {

    private static final String TRIP_NAME_PREFIX = "의 여행";

    private String name;

    protected TripName() {
    }

    public TripName(String name) {
        this.name = name;
    }

    public static TripName from(Member member) {
        return new TripName(member.nickname() + TRIP_NAME_PREFIX);
    }

    public String name() {
        return name;
    }
}
