package dev.tripdraw.post.application;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.file.application.FileUploader;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostCreateEvent;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.post.dto.PostAndPointCreateRequest;
import dev.tripdraw.post.dto.PostCreateResponse;
import dev.tripdraw.post.dto.PostRequest;
import dev.tripdraw.post.dto.PostResponse;
import dev.tripdraw.post.dto.PostSearchRequest;
import dev.tripdraw.post.dto.PostSearchResponse;
import dev.tripdraw.post.dto.PostUpdateRequest;
import dev.tripdraw.post.dto.PostsResponse;
import dev.tripdraw.post.dto.PostsSearchResponse;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostQueryService postQueryService;
    private final PostRepository postRepository;
    private final TripRepository tripRepository;
    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;
    private final FileUploader fileUploader;
    private final ApplicationEventPublisher applicationEventPublisher;

    public PostCreateResponse addAtCurrentPoint(
            LoginUser loginUser,
            PostAndPointCreateRequest postAndPointCreateRequest,
            MultipartFile file
    ) {
        Member member = memberRepository.getById(loginUser.memberId());
        Trip trip = tripRepository.getById(postAndPointCreateRequest.tripId());
        trip.validateAuthorization(member);
        Point point = pointRepository.save(postAndPointCreateRequest.toPoint(trip));

        Post post = postAndPointCreateRequest.toPost(member, point);
        uploadImage(file, post, trip);
        Post savedPost = postRepository.save(post);

        applicationEventPublisher.publishEvent(new PostCreateEvent(post.id(), trip.id()));
        return PostCreateResponse.from(savedPost);
    }

    private void uploadImage(MultipartFile file, Post post, Trip trip) {
        String filename = fileUploader.upload(file);
        if (filename == null) {
            return;
        }
        post.changePostImageUrl(filename);
        trip.changeImageUrl(filename);
    }

    public PostCreateResponse addAtExistingLocation(
            LoginUser loginUser,
            PostRequest postRequest,
            MultipartFile file
    ) {
        Member member = memberRepository.getById(loginUser.memberId());
        Trip trip = tripRepository.getById(postRequest.tripId());
        trip.validateAuthorization(member);
        Point point = pointRepository.getById(postRequest.pointId());

        Post post = postRequest.toPost(member, point);
        uploadImage(file, post, trip);
        Post savedPost = postRepository.save(post);

        applicationEventPublisher.publishEvent(new PostCreateEvent(post.id(), trip.id()));
        return PostCreateResponse.from(savedPost);
    }

    @Transactional(readOnly = true)
    public PostResponse read(LoginUser loginUser, Long postId) {
        Post post = postRepository.getByPostId(postId);
        Member member = memberRepository.getById(loginUser.memberId());
        post.validateAuthorization(member);
        return PostResponse.from(post);
    }

    @Transactional(readOnly = true)
    public PostsResponse readAllByTripId(LoginUser loginUser, Long tripId) {
        Member member = memberRepository.getById(loginUser.memberId());
        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(member);

        return postRepository.findAllByTripId(tripId).stream()
                .sorted(comparing(Post::pointRecordedAt).reversed())
                .collect(collectingAndThen(toList(), PostsResponse::from));
    }

    public void update(LoginUser loginUser, Long postId, PostUpdateRequest postUpdateRequest, MultipartFile file) {
        Post post = postRepository.getByPostId(postId);
        Member member = memberRepository.getById(loginUser.memberId());
        post.validateAuthorization(member);
        Trip trip = tripRepository.getById(post.tripId());
        trip.validateAuthorization(member);

        post.changeTitle(postUpdateRequest.title());
        post.changeWriting(postUpdateRequest.writing());
        uploadImage(file, post, trip);
    }

    public void delete(LoginUser loginUser, Long postId) {
        Post post = postRepository.getByPostId(postId);
        Member member = memberRepository.getById(loginUser.memberId());
        post.validateAuthorization(member);
        postRepository.deleteById(postId);
    }

    public PostsSearchResponse readAll(PostSearchRequest postSearchRequest) {
        List<Post> posts = postQueryService.findAllByConditions(postSearchRequest.conditions(), postSearchRequest.paging());

        List<PostSearchResponse> postSearchResponses = posts.stream()
                .map(PostSearchResponse::from)
                .toList();
        boolean hasNextPage = (posts.size() == postSearchRequest.paging().limit() + 1);

        return PostsSearchResponse.of(postSearchResponses, hasNextPage);
    }
}

