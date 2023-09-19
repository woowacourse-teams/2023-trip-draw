package dev.tripdraw.trip.domain;

import static dev.tripdraw.trip.exception.TripExceptionType.TRIP_NOT_FOUND;

import dev.tripdraw.trip.exception.TripException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TripRepository extends JpaRepository<Trip, Long>, TripCustomRepository {

    List<Trip> findAllByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);

    default Trip getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new TripException(TRIP_NOT_FOUND));
    }

    @Query("SELECT t FROM Trip t JOIN FETCH t.route.points where t.id = :tripId")
    Optional<Trip> findTripWithPoints(@Param("tripId") Long tripId);

    default Trip getTripWithPoints(Long tripId) {
        return findTripWithPoints(tripId)
                .orElseThrow(() -> new TripException(TRIP_NOT_FOUND));
    }
}
