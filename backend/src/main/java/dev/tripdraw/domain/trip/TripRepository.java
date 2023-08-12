package dev.tripdraw.domain.trip;

import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_NOT_FOUND;

import dev.tripdraw.exception.trip.TripException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findAllByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);

    default Trip getById(Long tripId) {
        return findById(tripId)
                .orElseThrow(() -> new TripException(TRIP_NOT_FOUND));
    }

    @Query("SELECT t FROM Trip t JOIN FETCH t.route.points where t.id = :tripId")
    Optional<Trip> findTripWithPoints(@Param("tripId") Long tripId);

    default Trip getTripWithPoints(Long tripId) {
        return findTripWithPoints(tripId)
                .orElseThrow(() -> new TripException(TRIP_NOT_FOUND));
    }
}
