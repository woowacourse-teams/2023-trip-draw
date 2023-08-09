package dev.tripdraw.application;

import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_NOT_FOUND;

import dev.tripdraw.application.draw.RouteImageGenerator;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.trip.Point;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.dto.trip.PointCreateRequest;
import dev.tripdraw.dto.trip.PointCreateResponse;
import dev.tripdraw.dto.trip.PointResponse;
import dev.tripdraw.dto.trip.TripCreateResponse;
import dev.tripdraw.dto.trip.TripResponse;
import dev.tripdraw.dto.trip.TripUpdateRequest;
import dev.tripdraw.dto.trip.TripsSearchResponse;
import dev.tripdraw.exception.member.MemberException;
import dev.tripdraw.exception.trip.TripException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TripService {

    private final TripRepository tripRepository;
    private final MemberRepository memberRepository;
    private final RouteImageGenerator routeImageGenerator;

    public TripService(
            TripRepository tripRepository,
            MemberRepository memberRepository,
            RouteImageGenerator routeImageGenerator
    ) {
        this.tripRepository = tripRepository;
        this.memberRepository = memberRepository;
        this.routeImageGenerator = routeImageGenerator;
    }

    public TripCreateResponse create(LoginUser loginUser) {
        Member member = getById(loginUser.memberId());
        Trip trip = Trip.from(member);
        Trip savedTrip = tripRepository.save(trip);
        return TripCreateResponse.from(savedTrip);
    }

    public PointCreateResponse addPoint(LoginUser loginUser, PointCreateRequest pointCreateRequest) {
        Member member = getById(loginUser.memberId());
        Trip trip = getByTripId(pointCreateRequest.tripId());

        Point point = pointCreateRequest.toPoint();
        trip.validateAuthorization(member);
        trip.add(point);

        tripRepository.flush();
        return PointCreateResponse.from(point);
    }

    public void deletePoint(LoginUser loginUser, Long pointId, Long tripId) {
        Member member = getById(loginUser.memberId());

        Trip trip = getByTripId(tripId);
        trip.validateAuthorization(member);

        trip.deletePointById(pointId);
    }

    private Member getById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    private Trip getByTripId(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new TripException(TRIP_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public TripResponse readTripById(LoginUser loginUser, Long id) {
        Member member = getById(loginUser.memberId());
        Trip trip = getByTripId(id);
        trip.validateAuthorization(member);
        return TripResponse.from(trip);
    }

    @Transactional(readOnly = true)
    public TripsSearchResponse readAllTrips(LoginUser loginUser) {
        Member member = getById(loginUser.memberId());
        List<Trip> trips = tripRepository.findAllByMemberId(member.id());
        return TripsSearchResponse.from(trips);
    }

    public void updateTripById(LoginUser loginUser, Long tripId, TripUpdateRequest tripUpdateRequest) {
        Member member = getById(loginUser.memberId());
        Trip trip = getByTripId(tripId);
        trip.validateAuthorization(member);

        trip.changeName(tripUpdateRequest.name());
        trip.changeStatus(tripUpdateRequest.status());
        String routeImageName = generateRouteImage(trip);
        trip.changeRouteImageUrl(routeImageName);
    }

    private String generateRouteImage(Trip trip) {
        return routeImageGenerator.generate(
                trip.getLatitudes(),
                trip.getLongitudes(),
                trip.getPointedLatitudes(),
                trip.getPointedLongitudes()
        );
    }

    @Transactional(readOnly = true)
    public PointResponse readPointByTripAndPointId(LoginUser loginUser, Long tripId, Long pointId) {
        Member member = getById(loginUser.memberId());
        Trip trip = getByTripId(tripId);
        trip.validateAuthorization(member);

        Point point = trip.findPointById(pointId);
        return PointResponse.from(point);
    }
}
