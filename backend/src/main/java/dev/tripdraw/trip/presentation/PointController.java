package dev.tripdraw.trip.presentation;

import static org.springframework.http.HttpStatus.CREATED;

import dev.tripdraw.common.auth.Auth;
import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.common.swagger.SwaggerAuthorizationRequired;
import dev.tripdraw.trip.application.TripService;
import dev.tripdraw.trip.dto.PointCreateRequest;
import dev.tripdraw.trip.dto.PointCreateResponse;
import dev.tripdraw.trip.dto.PointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Point", description = "위치정보 관련 API 명세")
@SwaggerAuthorizationRequired
@RequiredArgsConstructor
@RestController
public class PointController {

    private final TripService tripService;

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
}
