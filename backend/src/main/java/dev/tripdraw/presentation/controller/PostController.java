package dev.tripdraw.presentation.controller;

import static org.springframework.http.HttpStatus.CREATED;

import dev.tripdraw.application.PostService;
import dev.tripdraw.config.swagger.SwaggerLoginRequired;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.dto.post.PostPointCreateRequest;
import dev.tripdraw.dto.post.PostRequest;
import dev.tripdraw.dto.post.PostResponse;
import dev.tripdraw.presentation.member.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Post", description = "감상 관련 API 명세")
@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @SwaggerLoginRequired
    @Operation(summary = "현재 위치에 대한 감상 생성 API", description = "진행 중인 여행에서, 현재 위치에 대한 감상 생성")
    @PostMapping("/posts/current-location")
    public ResponseEntity<PostResponse> createOfCurrentLocation(
            @Auth LoginUser loginUser,
            @Valid @RequestBody PostPointCreateRequest postPointCreateRequest
    ) {
        PostResponse response = postService.createOfCurrentLocation(loginUser, postPointCreateRequest);
        return ResponseEntity.status(CREATED).body(response);
    }

    @SwaggerLoginRequired
    @Operation(summary = "사용자가 선택한 위치에 대한 감상 생성 API", description = "진행 중인 여행에서, 사용자가 선택한 위치에 대한 감상 생성")
    @PostMapping("/posts")
    public ResponseEntity<PostResponse> create(
            @Auth LoginUser loginUser,
            @Valid @RequestBody PostRequest postRequest
    ) {
        PostResponse response = postService.create(loginUser, postRequest);
        return ResponseEntity.status(CREATED).body(response);
    }
}
