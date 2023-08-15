package dev.tripdraw.domain.trip;

import static dev.tripdraw.exception.trip.TripExceptionType.POINT_NOT_FOUND;
import static dev.tripdraw.exception.trip.TripExceptionType.POINT_NOT_IN_TRIP;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import dev.tripdraw.exception.trip.TripException;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Route {

    @OneToMany(fetch = LAZY, cascade = {PERSIST, REMOVE})
    @JoinColumn(name = "trip_id", updatable = false, nullable = false)
    private List<Point> points = new ArrayList<>();

    public void add(Point point) {
        points.add(point);
    }

    public Point findPointById(Long pointIdToFind) {
        return points.stream()
                .filter(point -> Objects.equals(point.id(), pointIdToFind))
                .findAny()
                .orElseThrow(() -> new TripException(POINT_NOT_FOUND));
    }

    public void deletePointById(Long pointId) {
        Point pointToDelete = points.stream()
                .filter(point -> Objects.equals(point.id(), pointId))
                .findFirst()
                .orElseThrow(() -> new TripException(POINT_NOT_IN_TRIP));

        pointToDelete.delete();
    }

    public List<Point> points() {
        return points.stream()
                .filter(point -> !point.isDeleted())
                .toList();
    }
}
