package dev.tripdraw.trip.application;

import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripDeleteEvent;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.domain.TripUpdateEvent;
import dev.tripdraw.trip.dto.PointCreateRequest;
import dev.tripdraw.trip.dto.PointCreateResponse;
import dev.tripdraw.trip.dto.PointResponse;
import dev.tripdraw.trip.dto.TripCreateResponse;
import dev.tripdraw.trip.dto.TripResponse;
import dev.tripdraw.trip.dto.TripSearchConditions;
import dev.tripdraw.trip.dto.TripSearchRequest;
import dev.tripdraw.trip.dto.TripUpdateRequest;
import dev.tripdraw.trip.dto.TripsSearchResponse;
import dev.tripdraw.trip.dto.TripsSearchResponseOfMember;
import dev.tripdraw.trip.query.TripPaging;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class TripService {

    private static final int FIRST_INDEX = 0;

    private final TripRepository tripRepository;
    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;
    private final TripQueryService tripQueryService;
    private final ApplicationEventPublisher publisher;

    public TripCreateResponse create(LoginUser loginUser) {
        Long memberId = loginUser.memberId();

        Trip trip = Trip.of(memberId, memberRepository.getNicknameById(memberId));
        Trip savedTrip = tripRepository.save(trip);
        return TripCreateResponse.from(savedTrip);
    }

    public PointCreateResponse addPoint(LoginUser loginUser, PointCreateRequest pointCreateRequest) {
        Trip trip = tripRepository.getById(pointCreateRequest.tripId());
        trip.validateAuthorization(loginUser.memberId());

        Point point = pointCreateRequest.toPoint();
        point.setTrip(trip);
        pointRepository.save(point);

        return PointCreateResponse.from(point);
    }

    public void deletePoint(LoginUser loginUser, Long pointId, Long tripId) {
        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(loginUser.memberId());

        Point point = pointRepository.getById(pointId);
        pointRepository.delete(point);
    }

    @Transactional(readOnly = true)
    public TripResponse readTripById(LoginUser loginUser, Long id) {
        Trip trip = tripRepository.getById(id);
        trip.validateAuthorization(loginUser.memberId());
        return TripResponse.from(trip);
    }

    @Transactional(readOnly = true)
    public TripsSearchResponseOfMember readAllTripsOf(LoginUser loginUser) {
        List<Trip> trips = tripRepository.findAllByMemberId(loginUser.memberId());
        return TripsSearchResponseOfMember.from(trips);
    }

    @Transactional(readOnly = true)
    public TripsSearchResponse readAll(TripSearchRequest tripSearchRequest) {
        TripSearchConditions condition = tripSearchRequest.toTripSearchConditions();
        TripPaging tripPaging = tripSearchRequest.toTripPaging();

        List<Trip> trips = tripQueryService.readAllByQueryConditions(condition, tripPaging);

        if (tripPaging.hasNextPage(trips.size())) {
            return TripsSearchResponse.of(trips.subList(FIRST_INDEX, tripPaging.limit()), true);
        }
        return TripsSearchResponse.of(trips, false);
    }

    public void updateTripById(LoginUser loginUser, Long tripId, TripUpdateRequest tripUpdateRequest) {
        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(loginUser.memberId());

        trip.changeName(tripUpdateRequest.name());
        trip.changeStatus(tripUpdateRequest.status());

        publisher.publishEvent(new TripUpdateEvent(trip.id()));
    }

    @Transactional(readOnly = true)
    public PointResponse readPointByTripAndPointId(LoginUser loginUser, Long tripId, Long pointId) {
        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(loginUser.memberId());

        Point point = pointRepository.getById(pointId);
        return PointResponse.from(point);
    }

    public void delete(LoginUser loginUser, Long tripId) {
        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(loginUser.memberId());
        publisher.publishEvent(new TripDeleteEvent(tripId));
        tripRepository.delete(trip);
    }
}
