package dev.tripdraw.trip.query;

import dev.tripdraw.trip.domain.Trip;
import java.util.List;

public interface TripCustomRepository {

    List<Trip> findAllByConditions(TripQueryConditions tripQueryConditions, TripPaging tripPaging);
}
