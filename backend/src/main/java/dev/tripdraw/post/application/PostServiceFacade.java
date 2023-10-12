package dev.tripdraw.post.application;

import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.dto.PostAndPointCreateRequest;
import dev.tripdraw.post.dto.PostCreateResponse;
import dev.tripdraw.post.dto.PostRequest;
import dev.tripdraw.post.dto.PostResponse;
import dev.tripdraw.post.dto.PostSearchRequest;
import dev.tripdraw.post.dto.PostUpdateRequest;
import dev.tripdraw.post.dto.PostsResponse;
import dev.tripdraw.post.dto.PostsSearchResponse;
import dev.tripdraw.trip.domain.Trip;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class PostServiceFacade {

    private final PostService postService;
    private final PostFileUploader postFileUploader;

    public PostCreateResponse addAtCurrentPoint(
            LoginUser loginUser,
            PostAndPointCreateRequest postAndPointCreateRequest,
            MultipartFile file
    ) {
        Long memberId = loginUser.memberId();
        Trip trip = postService.getAuthorizedTrip(memberId, postAndPointCreateRequest.tripId());
        String imageUrl = postFileUploader.upload(file);
        return postService.addAtCurrentPoint(memberId, postAndPointCreateRequest, trip, imageUrl);
    }

    public PostCreateResponse addAtExistingLocation(
            LoginUser loginUser,
            PostRequest postRequest,
            MultipartFile file
    ) {
        Long memberId = loginUser.memberId();
        Trip trip = postService.getAuthorizedTrip(memberId, postRequest.tripId());
        String imageUrl = postFileUploader.upload(file);
        return postService.addAtExistingLocation(memberId, postRequest, trip, imageUrl);
    }

    public void update(LoginUser loginUser, Long postId, PostUpdateRequest postUpdateRequest, MultipartFile file) {
        Long memberId = loginUser.memberId();
        Post post = postService.getAuthroizedPost(memberId, postId);
        Trip trip = postService.getAuthorizedTrip(memberId, post.tripId());
        String imageUrl = postFileUploader.upload(file);
        postService.update(postUpdateRequest, post, trip, imageUrl);
    }

    public PostResponse read(Long postId) {
        return postService.read(postId);
    }

    public PostsResponse readAllByTripId(Long tripId) {
        return postService.readAllByTripId(tripId);
    }

    public void delete(LoginUser loginUser, Long postId) {
        Post post = postService.getAuthroizedPost(loginUser.memberId(), postId);
        postService.delete(post);
    }

    public PostsSearchResponse readAll(PostSearchRequest postSearchRequest) {
        return postService.readAll(postSearchRequest);
    }
}
