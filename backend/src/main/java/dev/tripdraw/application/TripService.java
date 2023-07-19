package dev.tripdraw.application;

import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.response.TripCreationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripCreationResponse create() {
        //TODO 로직 작성
        return new TripCreationResponse(1L);
    }
}
