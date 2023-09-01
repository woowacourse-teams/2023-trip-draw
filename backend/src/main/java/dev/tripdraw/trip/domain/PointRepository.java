package dev.tripdraw.trip.domain;

import dev.tripdraw.trip.exception.TripException;
import org.springframework.data.jpa.repository.JpaRepository;

import static dev.tripdraw.trip.exception.TripExceptionType.POINT_NOT_FOUND;

public interface PointRepository extends JpaRepository<Point, Long> {

    default Point getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new TripException(POINT_NOT_FOUND));
    }
}
