package dev.tripdraw.presentation.controller;

import static org.springframework.http.HttpStatus.CREATED;

import dev.tripdraw.application.TripService;
import dev.tripdraw.config.swagger.SwaggerAuthorizationRequired;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.dto.trip.PointCreateRequest;
import dev.tripdraw.dto.trip.PointCreateResponse;
import dev.tripdraw.dto.trip.PointDeleteRequest;
import dev.tripdraw.dto.trip.TripCreateResponse;
import dev.tripdraw.dto.trip.TripResponse;
import dev.tripdraw.dto.trip.TripUpdateRequest;
import dev.tripdraw.dto.trip.TripsSearchResponse;
import dev.tripdraw.dto.trip.TripUpdateRequest;
import dev.tripdraw.presentation.member.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Trip", description = "여행 관련 API 명세")
@SwaggerAuthorizationRequired
@RestController
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @Operation(summary = "여행 생성 API", description = "현재 로그인한 사용자의 여행을 생성합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "여행 생성 성공."
    )
    @PostMapping("/trips")
    public ResponseEntity<TripCreateResponse> create(@Auth LoginUser loginUser) {
        TripCreateResponse response = tripService.create(loginUser);
        return ResponseEntity.status(CREATED).body(response);
    }

    @Operation(summary = "위치 정보 저장 API", description = "현재 진행 중인 여행의 경로에 위치 정보를 저장합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "위치 정보 저장 성공."
    )
    @PostMapping("/points")
    public ResponseEntity<PointCreateResponse> addPoint(
            @Auth LoginUser loginUser,
            @RequestBody PointCreateRequest pointCreateRequest
    ) {
        PointCreateResponse response = tripService.addPoint(loginUser, pointCreateRequest);
        return ResponseEntity.status(CREATED).body(response);
    }

    @Operation(summary = "여행 조회 API", description = "단일 여행의 정보를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "여행 조회 성공."
    )
    @GetMapping("/trips/{tripId}")
    public ResponseEntity<TripResponse> readById(@Auth LoginUser loginUser, @PathVariable Long tripId) {
        TripResponse response = tripService.readTripById(loginUser, tripId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "위치정보 삭제 API", description = "특정 위치정보를 삭제합니다.")
    @ApiResponse(
            responseCode = "204",
            description = "위치정보 삭제 성공"
    )
    @DeleteMapping("/points")
    public ResponseEntity<Void> deletePoint(
            @Auth LoginUser loginUser,
            @RequestBody PointDeleteRequest pointDeleteRequest
    ) {
        tripService.deletePoint(loginUser, pointDeleteRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "여행 전체 조회 API", description = "모든 여행의 정보를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "여행 전체 조회 성공."
    )
    @GetMapping("/trips")
    public ResponseEntity<TripsSearchResponse> readAll(@Auth LoginUser loginUser) {
        TripsSearchResponse response = tripService.readAllTrips(loginUser);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "여행 이름 수정 및 종료 API", description = "여행 이름을 수정하고, 여행을 종료합니다.")
    @ApiResponse(
            responseCode = "204",
            description = "여행 이름 수정 및 종료 성공"
    )
    @PatchMapping("/trips/{tripId}")
    public ResponseEntity<Void> update(
            @Auth LoginUser loginUser,
            @PathVariable Long tripId,
            @RequestBody TripUpdateRequest tripUpdateRequest
    ) {
        tripService.updateTripById(loginUser, tripId, tripUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}
