package dev.tripdraw.trip.domain;

import static dev.tripdraw.trip.exception.TripExceptionType.POINT_NOT_FOUND;

import dev.tripdraw.trip.exception.TripException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointRepository extends JpaRepository<Point, Long> {

    default Point getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new TripException(POINT_NOT_FOUND));
    }

    @Modifying
    @Query("DELETE FROM Point p WHERE p.trip.id IN :tripIds")
    void deleteByTripIds(@Param(value = "tripIds") List<Long> tripIds);
}
