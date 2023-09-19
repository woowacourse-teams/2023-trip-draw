package dev.tripdraw.trip.presentation;

import dev.tripdraw.common.auth.Auth;
import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.common.swagger.SwaggerAuthorizationRequired;
import dev.tripdraw.trip.application.TripService;
import dev.tripdraw.trip.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Tag(name = "Trip", description = "여행 관련 API 명세")
@SwaggerAuthorizationRequired
@RequiredArgsConstructor
@RestController
public class TripController {

    private final TripService tripService;

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

    @Operation(summary = "위치 정보 조회 API", description = "위치 정보를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "위치 정보 조회 성공."
    )
    @GetMapping("/points/{pointId}")
    public ResponseEntity<PointResponse> readPointById(
            @Auth LoginUser loginUser,
            @PathVariable Long pointId,
            @RequestParam Long tripId
    ) {
        PointResponse response = tripService.readPointByTripAndPointId(loginUser, tripId, pointId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "나의 여행 조회 API", description = "회원 한 명의 단일 여행 정보를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "나의 여행 조회 성공."
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
    @DeleteMapping("/points/{pointId}")
    public ResponseEntity<Void> deletePoint(
            @Auth LoginUser loginUser,
            @PathVariable Long pointId,
            @RequestParam Long tripId
    ) {
        tripService.deletePoint(loginUser, pointId, tripId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "나의 여행 전체 조회 API", description = "회원 한 명의 모든 여행 정보를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "나의 여행 전체 조회 성공."
    )
    @GetMapping("/trips/me")
    public ResponseEntity<TripsSearchResponseOfMember> readAllOf(@Auth LoginUser loginUser) {
        TripsSearchResponseOfMember response = tripService.readAllTripsOf(loginUser);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "모든 회원 여행 전체 조회 API", description = "모든 회원의 여행 정보를 조건에 따라 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "모든 회원 여행 전체 조회 성공."
    )
    @GetMapping("/trips")
    public ResponseEntity<TripsSearchResponse> readAll(
            @Auth LoginUser loginUser,
            @RequestBody TripSearchRequest tripSearchRequest
    ) {
        TripsSearchResponse response = tripService.readAll(tripSearchRequest);
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

    @Operation(summary = "여행 삭제 API", description = "여행 삭제")
    @ApiResponse(
            responseCode = "204",
            description = "여행 삭제 성공."
    )
    @DeleteMapping("/trips/{tripId}")
    public ResponseEntity<Void> delete(
            @Auth LoginUser loginUser,
            @PathVariable Long tripId
    ) {
        tripService.delete(loginUser, tripId);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
