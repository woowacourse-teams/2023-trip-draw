package dev.tripdraw.domain.trip;

import static dev.tripdraw.exception.trip.TripExceptionType.NOT_AUTHORIZED;
import static jakarta.persistence.FetchType.LAZY;

import dev.tripdraw.domain.common.BaseEntity;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.exception.trip.TripException;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;

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

    protected Trip() {
    }

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
            throw new TripException(NOT_AUTHORIZED);
        }
    }

    public TripName name() {
        return name;
    }

    public String nameValue() {
        return name.name();
    }

    public Member member() {
        return member;
    }

    public Route route() {
        return route;
    }

    public List<Point> points() {
        return route.points();
    }
}
