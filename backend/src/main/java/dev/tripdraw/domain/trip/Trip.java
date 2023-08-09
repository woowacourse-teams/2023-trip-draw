package dev.tripdraw.domain.trip;

import static dev.tripdraw.domain.trip.TripStatus.ONGOING;
import static dev.tripdraw.exception.trip.TripExceptionType.NOT_AUTHORIZED_TO_TRIP;
import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_INVALID_STATUS;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import dev.tripdraw.domain.common.BaseEntity;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.exception.trip.TripException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;

@Entity
public class Trip extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "trip_id")
    private Long id;

    @Embedded
    private TripName name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Route route = new Route();

    @Column(nullable = false)
    private TripStatus status;

    private String imageUrl;

    private String routeImageUrl;

    protected Trip() {
    }

    public Trip(TripName name, Member member) {
        this(null, name, member, ONGOING);
    }

    public Trip(Long id, TripName name, Member member, TripStatus status) {
        this.id = id;
        this.name = name;
        this.member = member;
        this.status = status;
    }

    public static Trip from(Member member) {
        TripName tripName = TripName.from(member.nickname());
        return new Trip(tripName, member);
    }

    public void add(Point point) {
        route.add(point);
    }

    public void validateAuthorization(Member member) {
        if (!this.member.equals(member)) {
            throw new TripException(NOT_AUTHORIZED_TO_TRIP);
        }
    }

    public void changeStatus(TripStatus status) {
        validateStatus(status);
        this.status = status;
    }

    private void validateStatus(TripStatus status) {
        if (status == null) {
            throw new TripException(TRIP_INVALID_STATUS);
        }
    }

    public void changeName(String name) {
        this.name.change(name);
    }

    public Point findPointById(Long pointId) {
        return route.findPointById(pointId);
    }

    public void changeImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void changeRouteImageUrl(String routeImageUrl) {
        this.routeImageUrl = routeImageUrl;
    }

    public List<Double> getLatitudes() {
        return route.points().stream()
                .map(Point::latitude)
                .toList();
    }

    public List<Double> getLongitudes() {
        return route.points().stream()
                .map(Point::longitude)
                .toList();
    }

    public List<Double> getPointedLatitudes() {
        return route.points().stream()
                .filter(Point::hasPost)
                .map(Point::latitude)
                .toList();
    }

    public List<Double> getPointedLongitudes() {
        return route.points().stream()
                .filter(Point::hasPost)
                .map(Point::longitude)
                .toList();
    }

    public Long id() {
        return id;
    }

    public void deletePointById(Long pointId) {
        route.deletePointById(pointId);
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
        return route.points().stream()
                .filter(point -> !point.isDeleted())
                .toList();
    }

    public TripStatus status() {
        return status;
    }

    public String imageUrl() {
        return imageUrl;
    }

    public String routeImageUrl() {
        return routeImageUrl;
    }
}
