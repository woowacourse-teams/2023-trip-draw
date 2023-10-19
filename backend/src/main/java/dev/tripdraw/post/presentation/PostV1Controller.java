package dev.tripdraw.post.presentation;

import dev.tripdraw.common.auth.Auth;
import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.common.swagger.SwaggerAuthorizationRequired;
import dev.tripdraw.post.application.PostService;
import dev.tripdraw.post.dto.PostResponse;
import dev.tripdraw.post.dto.PostSearchRequest;
import dev.tripdraw.post.dto.PostsResponse;
import dev.tripdraw.post.dto.PostsSearchResponse;
import dev.tripdraw.post.dto.v1.PostResponseV1;
import dev.tripdraw.post.dto.v1.PostsResponseV1;
import dev.tripdraw.post.dto.v1.PostsSearchResponseV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Post", description = "감상 관련 API 명세")
@SwaggerAuthorizationRequired
@RequiredArgsConstructor
@RestController
public class PostV1Controller {

    private final PostService postService;

    @Operation(summary = "특정 감상 상세 조회 API", description = "특정한 1개의 감상을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "특정 감상 상세 조회 성공."
    )
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseV1> read(
            @Auth LoginUser loginUser,
            @PathVariable Long postId
    ) {
        PostResponse response = postService.read(loginUser, postId);
        PostResponseV1 responseV1 = PostResponseV1.from(response);
        return ResponseEntity.ok(responseV1);
    }

    @Operation(summary = "위치정보로 감상 조회 API", description = "특정한 1개의 감상을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "위치정보로 감상 조회 성공."
    )
    @GetMapping("/points/{pointId}/post")
    public ResponseEntity<PostResponseV1> readByPointId(
            @Auth LoginUser loginUser,
            @PathVariable Long pointId
    ) {
        PostResponse response = postService.readByPointId(loginUser, pointId);
        PostResponseV1 responseV1 = PostResponseV1.from(response);
        return ResponseEntity.ok(responseV1);
    }

    @Operation(summary = "특정 여행의 모든 감상 조회 API", description = "특정한 1개의 여행에 대해 작성한 모든 감상을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "특정 여행의 모든 감상 조회 성공."
    )
    @GetMapping("/trips/{tripId}/posts")
    public ResponseEntity<PostsResponseV1> readAllPostsOfTrip(
            @Auth LoginUser loginUser,
            @PathVariable Long tripId
    ) {
        PostsResponse response = postService.readAllByTripId(loginUser, tripId);
        PostsResponseV1 responseV1 = PostsResponseV1.from(response.posts(), loginUser.memberId());
        return ResponseEntity.ok(responseV1);
    }

    @Operation(summary = "모든 감상 조회 API", description = "모든 감상을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "모든 감상 조회 성공."
    )
    @GetMapping("/posts")
    public ResponseEntity<PostsSearchResponseV1> readAllPosts(
            @Auth LoginUser loginUser,
            PostSearchRequest postSearchRequest
    ) {
        PostsSearchResponse response = postService.readAll(loginUser, postSearchRequest);
        PostsSearchResponseV1 responseV1 = PostsSearchResponseV1.of(response.posts(), response.hasNextPage());
        return ResponseEntity.ok(responseV1);
    }
}
