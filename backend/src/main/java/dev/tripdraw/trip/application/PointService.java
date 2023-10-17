package dev.tripdraw.trip.application;

import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.dto.PointCreateRequest;
import dev.tripdraw.trip.dto.PointCreateResponse;
import dev.tripdraw.trip.dto.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PointService {

    private final TripRepository tripRepository;
    private final PointRepository pointRepository;

    public PointCreateResponse create(LoginUser loginUser, PointCreateRequest pointCreateRequest) {
        Trip trip = tripRepository.getByTripId(pointCreateRequest.tripId());
        trip.validateAuthorization(loginUser.memberId());

        Point point = pointCreateRequest.toPoint();
        point.setTrip(trip);
        pointRepository.save(point);

        return PointCreateResponse.from(point);
    }

    @Transactional(readOnly = true)
    public PointResponse read(LoginUser loginUser, Long tripId, Long pointId) {
        Trip trip = tripRepository.getByTripId(tripId);
        trip.validateAuthorization(loginUser.memberId());

        Point point = pointRepository.getById(pointId);
        return PointResponse.from(point);
    }

    public void delete(LoginUser loginUser, Long pointId, Long tripId) {
        Trip trip = tripRepository.getByTripId(tripId);
        trip.validateAuthorization(loginUser.memberId());

        Point point = pointRepository.getById(pointId);
        pointRepository.delete(point);
    }
}
