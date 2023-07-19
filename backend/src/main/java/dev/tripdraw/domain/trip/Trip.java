package dev.tripdraw.domain.trip;

import static dev.tripdraw.exception.ExceptionCode.NOT_AUTHORIZED;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import dev.tripdraw.domain.common.BaseEntity;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.exception.ForbiddenException;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Trip extends BaseEntity {

    private static final String TRIP_NAME_PREFIX = "의 여행";

    @Embedded
    private TripName name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Route route = new Route();

    @Builder
    public Trip(TripName name, Member member) {
        this.name = name;
        this.member = member;
    }

    public static Trip from(Member member) {
        return new Trip(TripName.from(member), member);
    }

    public void add(Point point) {
        route.add(point);
    }

    public void validateAuthorization(Member member) {
        if (!this.member.equals(member)) {
            throw new ForbiddenException(NOT_AUTHORIZED);
        }
    }
}
