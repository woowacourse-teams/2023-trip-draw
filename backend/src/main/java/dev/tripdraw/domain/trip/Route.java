package dev.tripdraw.domain.trip;

import static dev.tripdraw.exception.trip.TripExceptionType.POINT_NOT_FOUND;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;

import dev.tripdraw.exception.trip.TripException;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Route {

    @OneToMany(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "trip_id", updatable = false, nullable = false)
    private List<Point> points = new ArrayList<>();

    public Route() {
    }

    public void add(Point point) {
        points.add(point);
    }

    public List<Point> points() {
        return points;
    }

    public void deletePointById(Long pointId) {
        points.stream()
                .filter(point -> Objects.equals(point.id(), pointId))
                .findFirst()
                .orElseThrow(() -> new TripException(POINT_NOT_FOUND))
                .delete();
    }
}
