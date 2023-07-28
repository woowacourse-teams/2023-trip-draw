package dev.tripdraw.application;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.trip.Point;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.dto.trip.PointCreateRequest;
import dev.tripdraw.dto.trip.PointResponse;
import dev.tripdraw.dto.trip.TripResponse;
import dev.tripdraw.exception.member.MemberException;
import dev.tripdraw.exception.trip.TripException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_NOT_FOUND;

@Transactional
@Service
public class TripService {

    private final TripRepository tripRepository;
    private final MemberRepository memberRepository;

    public TripService(TripRepository tripRepository, MemberRepository memberRepository) {
        this.tripRepository = tripRepository;
        this.memberRepository = memberRepository;
    }

    public TripResponse create(LoginUser loginUser) {
        Member member = getByNickname(loginUser.nickname());
        Trip trip = Trip.from(member);
        Trip savedTrip = tripRepository.save(trip);
        return TripResponse.from(savedTrip);
    }

    public PointResponse addPoint(LoginUser loginUser, PointCreateRequest pointCreateRequest) {
        Member member = getByNickname(loginUser.nickname());
        Trip trip = getByTripId(pointCreateRequest.tripId());

        Point point = pointCreateRequest.toPoint();
        trip.validateAuthorization(member);
        trip.add(point);

        tripRepository.flush();
        return PointResponse.from(point);
    }

    private Member getByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    private Trip getByTripId(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new TripException(TRIP_NOT_FOUND));
    }

    public TripResponse readTripById(LoginUser loginUser, Long id) {
        Member member = getByNickname(loginUser.nickname());
        Trip trip = getByTripId(id);
        trip.validateAuthorization(member);
        return TripResponse.from(trip);
    }
}
