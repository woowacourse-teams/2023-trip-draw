package dev.tripdraw.trip.application;

import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.trip.domain.*;
import dev.tripdraw.trip.dto.*;
import dev.tripdraw.trip.query.TripPaging;
import dev.tripdraw.trip.query.TripQueryConditions;
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
    private final ApplicationEventPublisher applicationEventPublisher;

    public TripCreateResponse create(LoginUser loginUser) {
        Member member = memberRepository.getById(loginUser.memberId());
        Trip trip = Trip.from(member);
        Trip savedTrip = tripRepository.save(trip);
        return TripCreateResponse.from(savedTrip);
    }

    public PointCreateResponse addPoint(LoginUser loginUser, PointCreateRequest pointCreateRequest) {
        Member member = memberRepository.getById(loginUser.memberId());
        Trip trip = tripRepository.getById(pointCreateRequest.tripId());
        trip.validateAuthorization(member);

        Point point = pointCreateRequest.toPoint();
        point.setTrip(trip);
        pointRepository.save(point);

        return PointCreateResponse.from(point);
    }

    public void deletePoint(LoginUser loginUser, Long pointId, Long tripId) {
        Member member = memberRepository.getById(loginUser.memberId());
        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(member);

        Point point = pointRepository.getById(pointId);
        pointRepository.delete(point);
    }

    @Transactional(readOnly = true)
    public TripResponse readTripById(LoginUser loginUser, Long id) {
        Member member = memberRepository.getById(loginUser.memberId());
        Trip trip = tripRepository.getById(id);
        trip.validateAuthorization(member);
        return TripResponse.from(trip);
    }

    @Transactional(readOnly = true)
    public TripsSearchResponseOfMember readAllTripsOf(LoginUser loginUser) {
        Member member = memberRepository.getById(loginUser.memberId());
        List<Trip> trips = tripRepository.findAllByMemberId(member.id());
        return TripsSearchResponseOfMember.from(trips);
    }

    @Transactional(readOnly = true)
    public TripsSearchResponse readAll(TripSearchRequest tripSearchRequest) {
        TripQueryConditions tripQueryConditions = tripSearchRequest.toTripQueryConditions();
        TripPaging tripPaging = tripSearchRequest.toTripPaging();

        List<Trip> trips = tripQueryService.readAllByQueryConditions(tripQueryConditions, tripPaging);

        if (tripPaging.hasNextPage(trips.size())) {
            return TripsSearchResponse.of(trips.subList(FIRST_INDEX, tripPaging.limit()), true);
        }
        return TripsSearchResponse.of(trips, false);
    }

    public void updateTripById(LoginUser loginUser, Long tripId, TripUpdateRequest tripUpdateRequest) {
        Member member = memberRepository.getById(loginUser.memberId());
        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(member);

        trip.changeName(tripUpdateRequest.name());
        trip.changeStatus(tripUpdateRequest.status());

        applicationEventPublisher.publishEvent(new TripUpdateEvent(trip.id()));
    }

    @Transactional(readOnly = true)
    public PointResponse readPointByTripAndPointId(LoginUser loginUser, Long tripId, Long pointId) {
        Member member = memberRepository.getById(loginUser.memberId());
        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(member);

        Point point = pointRepository.getById(pointId);
        return PointResponse.from(point);
    }

    public void delete(LoginUser loginUser, Long tripId) {
        Member member = memberRepository.getById(loginUser.memberId());
        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(member);

        tripRepository.delete(trip);
    }
}
