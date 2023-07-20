package dev.tripdraw.presentation.controller;

import static org.springframework.http.HttpStatus.CREATED;

import dev.tripdraw.application.TripService;
import dev.tripdraw.config.swagger.SwaggerLoginRequired;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.dto.trip.PointCreateRequest;
import dev.tripdraw.dto.trip.PointResponse;
import dev.tripdraw.dto.trip.TripResponse;
import dev.tripdraw.presentation.member.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Trip", description = "여행 관련 API 명세")
@RestController
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @SwaggerLoginRequired
    @Operation(summary = "여행 생성 API", description = "현재 로그인한 사용자의 여행을 생성합니다.", tags = {"여행", "로그인"})
    @PostMapping("/trips")
    public ResponseEntity<TripResponse> create(@Auth LoginUser loginUser) {
        TripResponse response = tripService.create(loginUser);
        return ResponseEntity.status(CREATED).body(response);
    }

    @SwaggerLoginRequired
    @Operation(summary = "위치 정보 저장 API", description = "현재 진행 중인 여행의 경로에 위치 정보를 저장합니다..", tags = {"여행", "위치", "로그인"})
    @PostMapping("/points")
    public ResponseEntity<PointResponse> addPoint(
            @Auth LoginUser loginUser,
            @RequestBody PointCreateRequest pointCreateRequest
    ) {
        PointResponse response = tripService.addPoint(loginUser, pointCreateRequest);
        return ResponseEntity.status(CREATED).body(response);
    }
}
