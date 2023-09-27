package dev.tripdraw.trip.query;

import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.dto.TripSearchConditions;

import java.util.List;

public interface TripCustomRepository {

    List<Trip> findAllByConditions(TripSearchConditions tripSearchConditions, TripPaging tripPaging);
}
