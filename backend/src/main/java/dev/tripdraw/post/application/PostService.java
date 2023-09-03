package dev.tripdraw.post.application;

import static dev.tripdraw.file.domain.FileType.POST_IMAGE;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.file.application.FileUploader;
import dev.tripdraw.file.domain.FileType;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostCreateEvent;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.post.dto.PostAndPointCreateRequest;
import dev.tripdraw.post.dto.PostCreateResponse;
import dev.tripdraw.post.dto.PostRequest;
import dev.tripdraw.post.dto.PostResponse;
import dev.tripdraw.post.dto.PostUpdateRequest;
import dev.tripdraw.post.dto.PostsResponse;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

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
        Trip trip = findValidatedTripById(postAndPointCreateRequest.tripId(), member);

        Point point = postAndPointCreateRequest.toPoint();
        point.setTrip(trip);
        pointRepository.save(point);

        Post post = postAndPointCreateRequest.toPost(member, point);
        Post savedPost = postRepository.save(registerFileToPost(file, post));

        applicationEventPublisher.publishEvent(new PostCreateEvent(post.id(), trip.id()));

        return PostCreateResponse.from(savedPost);
    }

    public PostCreateResponse addAtExistingLocation(
            LoginUser loginUser,
            PostRequest postRequest,
            MultipartFile file
    ) {
        Member member = memberRepository.getById(loginUser.memberId());
        Trip trip = findValidatedTripById(postRequest.tripId(), member);
        Point point = pointRepository.getById(postRequest.pointId());

        Post post = postRequest.toPost(member, point);
        Post savedPost = postRepository.save(registerFileToPost(file, post));

        applicationEventPublisher.publishEvent(new PostCreateEvent(post.id(), trip.id()));

        return PostCreateResponse.from(savedPost);
    }

    public PostResponse read(LoginUser loginUser, Long postId) {
        Post post = postRepository.getById(postId);
        Member member = memberRepository.getById(loginUser.memberId());
        post.validateAuthorization(member);
        return PostResponse.from(post);
    }

    public PostsResponse readAllByTripId(LoginUser loginUser, Long tripId) {
        Member member = memberRepository.getById(loginUser.memberId());
        findValidatedTripById(tripId, member);

        return postRepository.findAllByTripId(tripId).stream()
                .sorted(Comparator.comparing(Post::pointRecordedAt).reversed())
                .collect(collectingAndThen(toList(), PostsResponse::from));
    }

    public void update(LoginUser loginUser, Long postId, PostUpdateRequest postUpdateRequest, MultipartFile file) {
        Post post = postRepository.getById(postId);
        Member member = memberRepository.getById(loginUser.memberId());
        post.validateAuthorization(member);

        post.changeTitle(postUpdateRequest.title());
        post.changeWriting(postUpdateRequest.writing());
        updateFileOfPost(file, post);
    }

    public void delete(LoginUser loginUser, Long postId) {
        Post post = postRepository.getById(postId);
        Member member = memberRepository.getById(loginUser.memberId());
        post.validateAuthorization(member);

        postRepository.deleteById(postId);
    }

    private void updateFileOfPost(MultipartFile file, Post post) {
        if (file == null) {
            return;
        }
        String imageUrl = fileUploader.upload(file, POST_IMAGE);
        post.changePostImageUrl(imageUrl);
    }

    private Trip findValidatedTripById(Long tripId, Member member) {
        Trip trip = tripRepository.getById(tripId);
        trip.validateAuthorization(member);
        return trip;
    }

    private Post registerFileToPost(MultipartFile file, Post post) {
        if (file == null) {
            return post;
        }
        FileType type = FileType.from(file.getContentType());
        String fileUrl = fileUploader.upload(file, type);

        post.changePostImageUrl(fileUrl);
        return post;
    }
}

