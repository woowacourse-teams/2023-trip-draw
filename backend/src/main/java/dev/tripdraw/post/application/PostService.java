package dev.tripdraw.post.application;

import static dev.tripdraw.trip.exception.TripExceptionType.TRIP_NOT_FOUND;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostCreateEvent;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.post.dto.PostAndPointCreateRequest;
import dev.tripdraw.post.dto.PostCreateResponse;
import dev.tripdraw.post.dto.PostRequest;
import dev.tripdraw.post.dto.PostResponse;
import dev.tripdraw.post.dto.PostSearchPaging;
import dev.tripdraw.post.dto.PostSearchRequest;
import dev.tripdraw.post.dto.PostSearchResponse;
import dev.tripdraw.post.dto.PostUpdateRequest;
import dev.tripdraw.post.dto.PostsResponse;
import dev.tripdraw.post.dto.PostsSearchResponse;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.exception.TripException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostQueryService postQueryService;
    private final PostRepository postRepository;
    private final TripRepository tripRepository;
    private final PointRepository pointRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public PostCreateResponse addAtCurrentPoint(
            Long memberId,
            PostAndPointCreateRequest postAndPointCreateRequest,
            Trip trip,
            String imageUrl
    ) {
        Point point = pointRepository.save(postAndPointCreateRequest.toPoint(trip));
        Post post = postAndPointCreateRequest.toPost(memberId, point);

        post.changePostImageUrl(imageUrl);
        trip.changeImageUrl(imageUrl);
        Post savedPost = postRepository.save(post);

        applicationEventPublisher.publishEvent(new PostCreateEvent(post.id(), trip.id()));
        return PostCreateResponse.from(savedPost);
    }

    public PostCreateResponse addAtExistingLocation(
            Long memberId,
            PostRequest postRequest,
            Trip trip,
            String imageUrl
    ) {
        Point point = pointRepository.getById(postRequest.pointId());
        Post post = postRequest.toPost(memberId, point);

        post.changePostImageUrl(imageUrl);
        trip.changeImageUrl(imageUrl);
        Post savedPost = postRepository.save(post);

        applicationEventPublisher.publishEvent(new PostCreateEvent(post.id(), trip.id()));
        return PostCreateResponse.from(savedPost);
    }

    @Transactional(readOnly = true)
    public PostResponse read(Long postId) {
        Post post = postRepository.getByPostId(postId);
        return PostResponse.from(post);
    }

    @Transactional(readOnly = true)
    public PostsResponse readAllByTripId(Long tripId) {
        if (!tripRepository.existsById(tripId)) {
            throw new TripException(TRIP_NOT_FOUND);
        }

        return postRepository.findAllByTripId(tripId).stream()
                .sorted(comparing(Post::pointRecordedAt).reversed())
                .collect(collectingAndThen(toList(), PostsResponse::from));
    }

    @Transactional(readOnly = true)
    public PostsSearchResponse readAll(PostSearchRequest postSearchRequest) {
        PostSearchPaging postSearchPaging = postSearchRequest.toPostSearchPaging();

        List<Post> posts = postQueryService.findAllByConditions(
                postSearchRequest.toPostSearchConditions(),
                postSearchPaging
        );

        List<PostSearchResponse> postSearchResponses = posts.stream()
                .map(PostSearchResponse::from)
                .toList();
        boolean hasNextPage = (posts.size() == postSearchPaging.limit() + 1);

        if (hasNextPage) {
            postSearchResponses = postSearchResponses.subList(0, postSearchPaging.limit());
        }

        return PostsSearchResponse.of(postSearchResponses, hasNextPage);
    }

    @Transactional(readOnly = true)
    public Trip getAuthorizedTrip(Long memberId, Long tripId) {
        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(memberId);
        return trip;
    }

    @Transactional(readOnly = true)
    public Post getAuthroizedPost(Long memberId, Long postId) {
        Post post = postRepository.getByPostId(postId);
        post.validateAuthorization(memberId);
        return post;
    }

    public void update(PostUpdateRequest postUpdateRequest, Post post, Trip trip, String imageUrl) {
        post.changeTitle(postUpdateRequest.title());
        post.changeWriting(postUpdateRequest.writing());
        post.changePostImageUrl(imageUrl);
        trip.changeImageUrl(imageUrl);
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }
}

