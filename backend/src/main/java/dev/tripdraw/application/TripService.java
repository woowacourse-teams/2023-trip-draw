package dev.tripdraw.application;

import static dev.tripdraw.exception.ExceptionCode.NOT_FOUND;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.trip.Point;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.dto.request.PointCreateRequest;
import dev.tripdraw.dto.response.PointCreateResponse;
import dev.tripdraw.dto.response.TripCreationResponse;
import dev.tripdraw.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class TripService {

    private final TripRepository tripRepository;
    private final MemberService memberService;

    public TripCreationResponse create(LoginUser loginUser) {
        Member member = memberService.validateLoginUser(loginUser);
        Trip trip = Trip.from(member);
        Trip savedTrip = tripRepository.save(trip);
        return new TripCreationResponse(savedTrip.getId());
    }

    public PointCreateResponse addPoint(LoginUser loginUser, PointCreateRequest pointCreateRequest) {
        Trip trip = tripRepository.findById(pointCreateRequest.tripId())
                .orElseThrow(() -> new BadRequestException(NOT_FOUND));
        Member member = memberService.validateLoginUser(loginUser);

        Point point = pointCreateRequest.toPoint();
        trip.validateAuthorization(member);
        trip.add(point);

        tripRepository.flush();
        return PointCreateResponse.from(point);
    }
}
