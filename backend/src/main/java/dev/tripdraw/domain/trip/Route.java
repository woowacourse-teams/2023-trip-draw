package dev.tripdraw.domain.trip;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
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
}
