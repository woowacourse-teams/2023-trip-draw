package dev.tripdraw.trip.application;

import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripDeleteEvent;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.domain.TripUpdateEvent;
import dev.tripdraw.trip.dto.TripCreateResponse;
import dev.tripdraw.trip.dto.TripResponse;
import dev.tripdraw.trip.dto.TripSearchConditions;
import dev.tripdraw.trip.dto.TripSearchRequest;
import dev.tripdraw.trip.dto.TripSearchResponse;
import dev.tripdraw.trip.dto.TripUpdateRequest;
import dev.tripdraw.trip.dto.TripsSearchResponse;
import dev.tripdraw.trip.dto.TripsSearchResponseOfMember;
import dev.tripdraw.trip.query.TripPaging;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TripService {

    private static final int FIRST_INDEX = 0;

    private final TripRepository tripRepository;
    private final MemberRepository memberRepository;
    private final TripQueryService tripQueryService;
    private final ApplicationEventPublisher publisher;

    public TripCreateResponse create(LoginUser loginUser) {
        Trip trip = Trip.of(memberRepository.getById(loginUser.memberId()));
        Trip savedTrip = tripRepository.save(trip);
        return TripCreateResponse.from(savedTrip);
    }

    @Transactional(readOnly = true)
    public TripResponse readTripById(LoginUser loginUser, Long id) {
        Trip trip = tripRepository.getById(id);
        return TripResponse.from(trip, loginUser.memberId());
    }

    @Transactional(readOnly = true)
    public TripsSearchResponseOfMember readAllTripsOf(LoginUser loginUser) {
        List<Trip> trips = tripRepository.findAllByMemberId(loginUser.memberId());
        return TripsSearchResponseOfMember.from(trips);
    }

    @Transactional(readOnly = true)
    public TripsSearchResponse readAll(LoginUser loginUser, TripSearchRequest tripSearchRequest) {
        TripSearchConditions conditions = tripSearchRequest.toTripSearchConditions();
        TripPaging tripPaging = tripSearchRequest.toTripPaging();

        List<Trip> trips = tripQueryService.readAllByConditions(conditions, tripPaging);

        List<TripSearchResponse> responses = trips.stream()
                .map(trip -> TripSearchResponse.from(trip, loginUser.memberId()))
                .toList();

        if (tripPaging.hasNextPage(responses.size())) {
            return TripsSearchResponse.of(responses.subList(FIRST_INDEX, tripPaging.limit()), true);
        }
        return TripsSearchResponse.of(responses, false);
    }

    public void updateTripById(LoginUser loginUser, Long tripId, TripUpdateRequest tripUpdateRequest) {
        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(loginUser.memberId());

        trip.changeName(tripUpdateRequest.name());
        trip.changeStatus(tripUpdateRequest.status());

        publisher.publishEvent(new TripUpdateEvent(trip.id()));
    }

    public void delete(LoginUser loginUser, Long tripId) {
        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(loginUser.memberId());
        publisher.publishEvent(new TripDeleteEvent(tripId));
        tripRepository.delete(trip);
    }
}
