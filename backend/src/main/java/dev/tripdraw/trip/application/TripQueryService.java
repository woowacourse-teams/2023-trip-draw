package dev.tripdraw.trip.application;

import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.dto.TripSearchConditions;
import dev.tripdraw.trip.query.TripCustomRepository;
import dev.tripdraw.trip.query.TripPaging;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TripQueryService {

    private final TripCustomRepository tripCustomRepository;

    @Transactional(readOnly = true)
    public List<Trip> readAllByConditions(TripSearchConditions tripSearchConditions, TripPaging tripPaging) {
        return tripCustomRepository.findAllByConditions(tripSearchConditions, tripPaging);
    }
}

