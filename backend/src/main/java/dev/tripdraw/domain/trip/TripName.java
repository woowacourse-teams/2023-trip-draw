package dev.tripdraw.domain.trip;

import static lombok.AccessLevel.PROTECTED;

import dev.tripdraw.domain.member.Member;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class TripName {

    private static final String TRIP_NAME_PREFIX = "의 여행";

    private String name;

    public TripName(String name) {
        this.name = name;
    }

    public static TripName from(Member member) {
        return new TripName(member.getNickname() + TRIP_NAME_PREFIX);
    }
}
