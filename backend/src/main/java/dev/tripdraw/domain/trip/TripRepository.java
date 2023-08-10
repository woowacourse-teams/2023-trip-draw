package dev.tripdraw.domain.trip;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findAllByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);
}
