package dev.tripdraw.application;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.trip.Point;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.domain.trip.TripUpdateEvent;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.dto.trip.PointCreateRequest;
import dev.tripdraw.dto.trip.PointCreateResponse;
import dev.tripdraw.dto.trip.PointResponse;
import dev.tripdraw.dto.trip.TripCreateResponse;
import dev.tripdraw.dto.trip.TripResponse;
import dev.tripdraw.dto.trip.TripUpdateRequest;
import dev.tripdraw.dto.trip.TripsSearchResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TripService {

    private final TripRepository tripRepository;
    private final MemberRepository memberRepository;
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

        Point point = pointCreateRequest.toPoint();
        trip.validateAuthorization(member);
        trip.add(point);

        tripRepository.flush();
        return PointCreateResponse.from(point);
    }

    public void deletePoint(LoginUser loginUser, Long pointId, Long tripId) {
        Member member = memberRepository.getById(loginUser.memberId());

        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(member);

        trip.deletePointById(pointId);
    }

    @Transactional(readOnly = true)
    public TripResponse readTripById(LoginUser loginUser, Long id) {
        Member member = memberRepository.getById(loginUser.memberId());
        Trip trip = tripRepository.getById(id);
        trip.validateAuthorization(member);
        return TripResponse.from(trip);
    }

    @Transactional(readOnly = true)
    public TripsSearchResponse readAllTrips(LoginUser loginUser) {
        Member member = memberRepository.getById(loginUser.memberId());
        List<Trip> trips = tripRepository.findAllByMemberId(member.id());
        return TripsSearchResponse.from(trips);
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

        Point point = trip.findPointById(pointId);
        return PointResponse.from(point);
    }

    public void delete(LoginUser loginUser, Long tripId) {
        Member member = memberRepository.getById(loginUser.memberId());
        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(member);

        tripRepository.delete(trip);
    }
}
