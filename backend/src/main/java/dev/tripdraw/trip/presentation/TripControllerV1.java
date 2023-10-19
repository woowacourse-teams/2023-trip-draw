package dev.tripdraw.trip.presentation;

import dev.tripdraw.common.auth.Auth;
import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.common.swagger.SwaggerAuthorizationRequired;
import dev.tripdraw.trip.application.TripService;
import dev.tripdraw.trip.dto.TripResponse;
import dev.tripdraw.trip.dto.TripSearchRequest;
import dev.tripdraw.trip.dto.TripsSearchResponse;
import dev.tripdraw.trip.dto.v1.TripResponseV1;
import dev.tripdraw.trip.dto.v1.TripsSearchResponseV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Trip", description = "여행 관련 API 명세")
@SwaggerAuthorizationRequired
@RequiredArgsConstructor
@RequestMapping("/trips")
@RestController
public class TripControllerV1 {

    private final TripService tripService;

    @Operation(summary = "나의 여행 조회 API", description = "회원 한 명의 단일 여행 정보를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "나의 여행 조회 성공."
    )
    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponseV1> readById(@Auth LoginUser loginUser, @PathVariable Long tripId) {
        TripResponse response = tripService.readTripById(loginUser, tripId);
        TripResponseV1 tripResponseV1 = TripResponseV1.from(response);
        return ResponseEntity.ok(tripResponseV1);
    }

    @Operation(summary = "모든 회원 여행 전체 조회 API", description = "모든 회원의 여행 정보를 조건에 따라 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "모든 회원 여행 전체 조회 성공."
    )
    @GetMapping
    public ResponseEntity<TripsSearchResponseV1> readAll(
            @Auth LoginUser loginUser,
            TripSearchRequest tripSearchRequest
    ) {
        TripsSearchResponse response = tripService.readAll(loginUser, tripSearchRequest);
        return ResponseEntity.ok(TripsSearchResponseV1.of(response.trips(), response.hasNextPage()));
    }
}
