package dev.tripdraw.domain.trip;

import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_NOT_FOUND;

import dev.tripdraw.exception.trip.TripException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findAllByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);

    default Trip getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new TripException(TRIP_NOT_FOUND));
    }
}
