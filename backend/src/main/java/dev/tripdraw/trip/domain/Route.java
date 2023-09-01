package dev.tripdraw.trip.domain;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Route {

    @OneToMany(mappedBy = "trip", fetch = LAZY, cascade = {PERSIST, REMOVE})
    private List<Point> points = new ArrayList<>();

    public void add(Point point) {
        points.add(point);
    }

    public boolean contains(Point point) {
        return points.stream()
                .anyMatch(it -> it == point);
    }
}
