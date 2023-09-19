package dev.tripdraw.trip.domain;

import dev.tripdraw.common.domain.Paging;
import java.util.List;

public interface TripCustomRepository {

    List<Trip> findAllByConditions(SearchConditions searchConditions, Paging paging);
}
