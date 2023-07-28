package dev.tripdraw.presentation.controller;

import static org.springframework.http.HttpStatus.CREATED;

import dev.tripdraw.application.PostService;
import dev.tripdraw.config.swagger.SwaggerAuthorizationRequired;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.dto.post.PostAndPointCreateRequest;
import dev.tripdraw.dto.post.PostCreateResponse;
import dev.tripdraw.dto.post.PostRequest;
import dev.tripdraw.presentation.member.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Post", description = "감상 관련 API 명세")
@SwaggerAuthorizationRequired
@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "현재 위치에 대한 감상 생성 API", description = "진행 중인 여행에서, 현재 위치에 대한 감상 생성")
    @ApiResponse(
            responseCode = "201",
            description = "현재 위치에 대한 감상 생성 성공."
    )
    @PostMapping("/posts/current-location")
    public ResponseEntity<PostCreateResponse> createOfCurrentLocation(
            @Auth LoginUser loginUser,
            @Valid @RequestBody PostAndPointCreateRequest postAndPointCreateRequest
    ) {
        PostCreateResponse response = postService.addAtCurrentPoint(loginUser, postAndPointCreateRequest);
        return ResponseEntity.status(CREATED).body(response);
    }

    @Operation(summary = "사용자가 선택한 위치에 대한 감상 생성 API", description = "진행 중인 여행에서, 사용자가 선택한 위치에 대한 감상 생성")
    @ApiResponse(
            responseCode = "201",
            description = "사용자가 선택한 위치에 대한 감상 생성 성공."
    )
    @PostMapping("/posts")
    public ResponseEntity<PostCreateResponse> create(
            @Auth LoginUser loginUser,
            @Valid @RequestBody PostRequest postRequest
    ) {
        PostCreateResponse response = postService.addAtExistingLocation(loginUser, postRequest);
        return ResponseEntity.status(CREATED).body(response);
    }
}
