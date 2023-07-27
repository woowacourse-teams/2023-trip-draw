package dev.tripdraw.presentation.controller;

import static org.springframework.http.HttpStatus.CREATED;

import dev.tripdraw.application.TripService;
import dev.tripdraw.config.swagger.SwaggerLoginRequired;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.dto.trip.PointCreateRequest;
import dev.tripdraw.dto.trip.PointDeleteRequest;
import dev.tripdraw.dto.trip.PointResponse;
import dev.tripdraw.dto.trip.TripResponse;
import dev.tripdraw.presentation.member.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Trip", description = "여행 관련 API 명세")
@SwaggerLoginRequired
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
    public ResponseEntity<TripResponse> create(@Auth LoginUser loginUser) {
        TripResponse response = tripService.create(loginUser);
        return ResponseEntity.status(CREATED).body(response);
    }

    @Operation(summary = "위치 정보 저장 API", description = "현재 진행 중인 여행의 경로에 위치 정보를 저장합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "위치 정보 저장 성공."
    )
    @PostMapping("/points")
    public ResponseEntity<PointResponse> addPoint(
            @Auth LoginUser loginUser,
            @RequestBody PointCreateRequest pointCreateRequest
    ) {
        PointResponse response = tripService.addPoint(loginUser, pointCreateRequest);
        return ResponseEntity.status(CREATED).body(response);
    }

    @SwaggerLoginRequired
    @Operation(summary = "위치 정보 삭제 API", description = "특정 위치 정보를 삭제합니다.")
    @DeleteMapping("/points")
    public ResponseEntity<Void> deletePoint(
            @Auth LoginUser loginUser,
            @RequestBody PointDeleteRequest pointDeleteRequest
    ) {
        tripService.deletePoint(loginUser, pointDeleteRequest);
        return ResponseEntity.noContent().build();
    }
}
