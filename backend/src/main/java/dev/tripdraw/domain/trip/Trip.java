package dev.tripdraw.domain.trip;

import static dev.tripdraw.domain.trip.TripStatus.ONGOING;
import static dev.tripdraw.exception.trip.TripExceptionType.NOT_AUTHORIZED_TO_TRIP;
import static dev.tripdraw.exception.trip.TripExceptionType.ONE_OR_NO_POINT;
import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_INVALID_STATUS;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

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
import java.time.Duration;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Trip extends BaseEntity {

    private static final String EMPTY_IMAGE_URL = "";

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

    public Trip(TripName name, Member member) {
        this(null, name, member, ONGOING, EMPTY_IMAGE_URL, EMPTY_IMAGE_URL);
    }

    public Trip(Long id, TripName name, Member member, TripStatus status, String imageUrl, String routeImageUrl) {
        this.id = id;
        this.name = name;
        this.member = member;
        this.status = status;
        this.imageUrl = imageUrl;
        this.routeImageUrl = routeImageUrl;
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

    public TripDuration calculateTripDuration() {
        List<Point> points = points();
        if (points.size() == 1 || points.isEmpty()) {
            throw new TripException(ONE_OR_NO_POINT);
        }
        Point startingPoint = points.get(0);
        Point arrivalPoint = points.get(points.size() - 1);

        Duration between = Duration.between(startingPoint.recordedAt(), arrivalPoint.recordedAt());
        return TripDuration.of(between);
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

    public void deletePointById(Long pointId) {
        route.deletePointById(pointId);
    }

    public List<Point> points() {
        return route.points();
    }

    public String nameValue() {
        return name.name();
    }
}
