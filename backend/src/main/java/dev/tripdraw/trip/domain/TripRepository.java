package dev.tripdraw.trip.domain;

import dev.tripdraw.trip.exception.TripException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static dev.tripdraw.trip.exception.TripExceptionType.TRIP_NOT_FOUND;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findAllByMemberId(Long memberId);

    @Modifying
    @Query("DELETE FROM Trip t WHERE t.memberId = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

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

    @Query("SELECT t.id FROM Trip t WHERE t.memberId = :memberId")
    List<Long> findAllTripIdsByMemberId(@Param("memberId") Long memberId);
}
