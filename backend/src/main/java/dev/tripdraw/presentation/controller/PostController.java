package dev.tripdraw.presentation.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import dev.tripdraw.application.PostService;
import dev.tripdraw.config.swagger.SwaggerAuthorizationRequired;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.dto.post.PostAndPointCreateRequest;
import dev.tripdraw.dto.post.PostCreateResponse;
import dev.tripdraw.dto.post.PostRequest;
import dev.tripdraw.dto.post.PostResponse;
import dev.tripdraw.dto.post.PostUpdateRequest;
import dev.tripdraw.dto.post.PostsResponse;
import dev.tripdraw.presentation.member.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
            @Valid @RequestPart(value = "dto") PostAndPointCreateRequest postAndPointCreateRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        PostCreateResponse response = postService.addAtCurrentPoint(loginUser, postAndPointCreateRequest, file);
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
            @Valid @RequestPart(value = "dto") PostRequest postRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        PostCreateResponse response = postService.addAtExistingLocation(loginUser, postRequest, file);
        return ResponseEntity.status(CREATED).body(response);
    }

    @Operation(summary = "특정 감상 상세 조회 API", description = "특정한 1개 감상의 상세 정보 조회")
    @ApiResponse(
            responseCode = "200",
            description = "특정 감상 상세 조회 성공."
    )
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> read(
            @Auth LoginUser loginUser,
            @PathVariable Long postId
    ) {
        PostResponse response = postService.read(loginUser, postId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 여행의 모든 감상 조회 API", description = "특정한 1개의 여행에 대해 작성한 모든 감상 정보 조회")
    @ApiResponse(
            responseCode = "200",
            description = "특정 여행의 모든 감상 조회 성공."
    )
    @GetMapping("/trips/{tripId}/posts")
    public ResponseEntity<PostsResponse> readAllPostsOfTrip(
            @Auth LoginUser loginUser,
            @PathVariable Long tripId
    ) {
        PostsResponse response = postService.readAllByTripId(loginUser, tripId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "감상 수정 API", description = "주소를 제외한 감상의 모든 정보를 수정")
    @ApiResponse(
            responseCode = "204",
            description = "감상 수정 성공."
    )
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<Void> update(
            @Auth LoginUser loginUser,
            @PathVariable Long postId,
            @Valid @RequestPart(value = "dto") PostUpdateRequest postUpdateRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        postService.update(loginUser, postId, postUpdateRequest, file);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @Operation(summary = "감상 삭제 API", description = "감상 삭제")
    @ApiResponse(
            responseCode = "204",
            description = "감상 삭제 성공."
    )
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> delete(
            @Auth LoginUser loginUser,
            @PathVariable Long postId
    ) {
        postService.delete(loginUser, postId);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
