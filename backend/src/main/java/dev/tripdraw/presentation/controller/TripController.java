package dev.tripdraw.presentation.controller;

import dev.tripdraw.application.TripService;
import dev.tripdraw.dto.response.TripCreationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/trips")
@RestController
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<TripCreationResponse> create() {
        TripCreationResponse tripCreationResponse = tripService.create();
    }
}
