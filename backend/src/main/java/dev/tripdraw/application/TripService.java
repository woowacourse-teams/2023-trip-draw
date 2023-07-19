package dev.tripdraw.application;

import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_NOT_FOUND;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.trip.Point;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.dto.request.PointCreateRequest;
import dev.tripdraw.dto.response.PointCreateResponse;
import dev.tripdraw.dto.response.TripCreateResponse;
import dev.tripdraw.exception.trip.TripException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class TripService {

    private final TripRepository tripRepository;
    private final MemberService memberService;

    public TripService(TripRepository tripRepository, MemberService memberService) {
        this.tripRepository = tripRepository;
        this.memberService = memberService;
    }

    public TripCreateResponse create(LoginUser loginUser) {
        Member member = memberService.validateLoginUser(loginUser);
        Trip trip = Trip.from(member);
        Trip savedTrip = tripRepository.save(trip);
        return TripCreateResponse.from(savedTrip);
    }

    public PointCreateResponse addPoint(LoginUser loginUser, PointCreateRequest pointCreateRequest) {
        Trip trip = tripRepository.findById(pointCreateRequest.tripId())
                .orElseThrow(() -> new TripException(TRIP_NOT_FOUND));
        Member member = memberService.validateLoginUser(loginUser);

        Point point = pointCreateRequest.toPoint();
        trip.validateAuthorization(member);
        trip.add(point);

        tripRepository.flush();
        return PointCreateResponse.from(point);
    }
}
