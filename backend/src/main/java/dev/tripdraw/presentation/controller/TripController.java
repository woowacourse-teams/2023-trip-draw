package dev.tripdraw.presentation.controller;

import static org.springframework.http.HttpStatus.CREATED;

import dev.tripdraw.application.TripService;
import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.dto.request.PointCreateRequest;
import dev.tripdraw.dto.response.PointCreateResponse;
import dev.tripdraw.dto.response.TripCreateResponse;
import dev.tripdraw.presentation.member.Auth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/trips")
    public ResponseEntity<TripCreateResponse> create(@Auth LoginUser loginUser) {
        TripCreateResponse response = tripService.create(loginUser);
        return ResponseEntity.status(CREATED).body(response);
    }

    @PostMapping("/points")
    public ResponseEntity<PointCreateResponse> addPoint(
            @Auth LoginUser loginUser,
            @RequestBody PointCreateRequest pointCreateRequest
    ) {
        PointCreateResponse response = tripService.addPoint(loginUser, pointCreateRequest);
        return ResponseEntity.status(CREATED).body(response);
    }
}
