package dev.tripdraw.trip.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class TripName {

    private static final String TRIP_NAME_PREFIX = "의 여행";

    private String name;

    public TripName(String name) {
        this.name = name;
    }

    public static TripName from(String nickname) {
        return new TripName(nickname + TRIP_NAME_PREFIX);
    }

    public void change(String name) {
        this.name = name;
    }
}
