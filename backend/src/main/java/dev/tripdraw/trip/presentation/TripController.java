package dev.tripdraw.trip.presentation;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import dev.tripdraw.common.auth.Auth;
import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.common.swagger.SwaggerAuthorizationRequired;
import dev.tripdraw.trip.application.TripService;
import dev.tripdraw.trip.dto.TripCreateResponse;
import dev.tripdraw.trip.dto.TripResponse;
import dev.tripdraw.trip.dto.TripSearchRequest;
import dev.tripdraw.trip.dto.TripUpdateRequest;
import dev.tripdraw.trip.dto.TripsSearchResponse;
import dev.tripdraw.trip.dto.TripsSearchResponseOfMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Trip", description = "여행 관련 API 명세")
@SwaggerAuthorizationRequired
@RequiredArgsConstructor
@RequestMapping("/trips")
@RestController
public class TripController {

    private final TripService tripService;

    @Operation(summary = "여행 생성 API", description = "현재 로그인한 사용자의 여행을 생성합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "여행 생성 성공."
    )
    @PostMapping
    public ResponseEntity<TripCreateResponse> create(@Auth LoginUser loginUser) {
        TripCreateResponse response = tripService.create(loginUser);
        return ResponseEntity.status(CREATED).body(response);
    }

    @Operation(summary = "나의 여행 조회 API", description = "회원 한 명의 단일 여행 정보를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "나의 여행 조회 성공."
    )
    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponse> readById(@PathVariable Long tripId) {
        TripResponse response = tripService.readTripById(tripId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "나의 여행 전체 조회 API", description = "회원 한 명의 모든 여행 정보를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "나의 여행 전체 조회 성공."
    )
    @GetMapping("/me")
    public ResponseEntity<TripsSearchResponseOfMember> readAllOf(@Auth LoginUser loginUser) {
        TripsSearchResponseOfMember response = tripService.readAllTripsOf(loginUser);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "모든 회원 여행 전체 조회 API", description = "모든 회원의 여행 정보를 조건에 따라 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "모든 회원 여행 전체 조회 성공."
    )
    @GetMapping
    public ResponseEntity<TripsSearchResponse> readAll(TripSearchRequest tripSearchRequest) {
        TripsSearchResponse response = tripService.readAll(tripSearchRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "여행 이름 수정 및 종료 API", description = "여행 이름을 수정하고, 여행을 종료합니다.")
    @ApiResponse(
            responseCode = "204",
            description = "여행 이름 수정 및 종료 성공"
    )
    @PatchMapping("/{tripId}")
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
    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> delete(
            @Auth LoginUser loginUser,
            @PathVariable Long tripId
    ) {
        tripService.delete(loginUser, tripId);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
