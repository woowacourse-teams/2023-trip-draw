package dev.tripdraw.trip.application;

import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.domain.TripUpdateEvent;
import dev.tripdraw.auth.dto.LoginUser;
import dev.tripdraw.trip.dto.PointCreateRequest;
import dev.tripdraw.trip.dto.PointCreateResponse;
import dev.tripdraw.trip.dto.PointResponse;
import dev.tripdraw.trip.dto.TripCreateResponse;
import dev.tripdraw.trip.dto.TripResponse;
import dev.tripdraw.trip.dto.TripUpdateRequest;
import dev.tripdraw.trip.dto.TripsSearchResponse;
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
