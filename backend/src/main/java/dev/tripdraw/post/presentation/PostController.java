package dev.tripdraw.post.presentation;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import dev.tripdraw.common.auth.Auth;
import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.common.swagger.SwaggerAuthorizationRequired;
import dev.tripdraw.post.application.PostService;
import dev.tripdraw.post.dto.PostAndPointCreateRequest;
import dev.tripdraw.post.dto.PostCreateResponse;
import dev.tripdraw.post.dto.PostRequest;
import dev.tripdraw.post.dto.PostResponse;
import dev.tripdraw.post.dto.PostUpdateRequest;
import dev.tripdraw.post.dto.PostsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Post", description = "감상 관련 API 명세")
@SwaggerAuthorizationRequired
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @Operation(summary = "현재 위치에 대한 감상 생성 API", description = "현재 위치에 대한 감상을 생성합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "현재 위치에 대한 감상 생성 성공."
    )
    @PostMapping(
            path = "/posts/current-location",
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PostCreateResponse> createOfCurrentLocation(
            @Auth LoginUser loginUser,

            @Parameter(description = "감상 정보를 담은 JSON 객체")
            @Valid
            @RequestPart(value = "dto")
            PostAndPointCreateRequest postAndPointCreateRequest,

            @Parameter(description = "감상에 등록할 이미지 파일")
            @RequestPart(value = "file", required = false)
            MultipartFile file
    ) {
        PostCreateResponse response = postService.addAtCurrentPoint(loginUser, postAndPointCreateRequest, file);
        return ResponseEntity.status(CREATED).body(response);
    }

    @Operation(summary = "사용자가 선택한 위치에 대한 감상 생성 API", description = "사용자가 선택한 위치에 대한 감상을 생성합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "사용자가 선택한 위치에 대한 감상 생성 성공."
    )
    @PostMapping(
            path = "/posts",
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PostCreateResponse> create(
            @Auth
            LoginUser loginUser,

            @Parameter(description = "감상 정보를 담은 JSON 객체")
            @Valid
            @RequestPart(value = "dto")
            PostRequest postRequest,

            @Parameter(description = "감상에 등록할 이미지 파일")
            @RequestParam(value = "file", required = false)
            MultipartFile file
    ) {
        PostCreateResponse response = postService.addAtExistingLocation(loginUser, postRequest, file);
        return ResponseEntity.status(CREATED).body(response);
    }

    @Operation(summary = "특정 감상 상세 조회 API", description = "특정한 1개의 감상을 조회합니다.")
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

    @Operation(summary = "특정 여행의 모든 감상 조회 API", description = "특정한 1개의 여행에 대해 작성한 모든 감상을 조회합니다.")
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

    @Operation(summary = "감상 수정 API", description = "주소를 제외한 감상의 모든 정보를 수정합니다.")
    @ApiResponse(
            responseCode = "204",
            description = "감상 수정 성공."
    )
    @PatchMapping(
            path = "/posts/{postId}",
            consumes = MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> update(
            @Auth
            LoginUser loginUser,

            @PathVariable Long postId,

            @Parameter(description = "수정할 감상 정보를 담은 JSON 객체")
            @Valid @RequestPart(value = "dto")
            PostUpdateRequest postUpdateRequest,

            @Parameter(description = "감상에 등록할 이미지 파일")
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        postService.update(loginUser, postId, postUpdateRequest, file);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @Operation(summary = "감상 삭제 API", description = "특정한 1개의 감상을 삭제합니다.")
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
