package dev.tripdraw.application;

import static dev.tripdraw.domain.file.FileType.POST_IMAGE;
import static dev.tripdraw.exception.post.PostExceptionType.POST_NOT_FOUNT;
import static dev.tripdraw.exception.post.PostExceptionType.POST_NOT_FOUND;
import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_NOT_FOUND;

import dev.tripdraw.application.file.FileUploader;
import dev.tripdraw.domain.file.FileType;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.post.Post;
import dev.tripdraw.domain.post.PostCreateEvent;
import dev.tripdraw.domain.post.PostRepository;
import dev.tripdraw.domain.trip.Point;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.dto.post.PostAndPointCreateRequest;
import dev.tripdraw.dto.post.PostCreateResponse;
import dev.tripdraw.dto.post.PostRequest;
import dev.tripdraw.dto.post.PostResponse;
import dev.tripdraw.dto.post.PostUpdateRequest;
import dev.tripdraw.dto.post.PostsResponse;
import dev.tripdraw.exception.post.PostException;
import dev.tripdraw.exception.trip.TripException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
        trip.add(point);
        tripRepository.flush();

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

        Point point = trip.findPointById(postRequest.pointId());

        Post post = postRequest.toPost(member, point);
        Post savedPost = postRepository.save(registerFileToPost(file, post));

        applicationEventPublisher.publishEvent(new PostCreateEvent(post.id(), trip.id()));

        return PostCreateResponse.from(savedPost);
    }

    public PostResponse read(LoginUser loginUser, Long postId) {
        Post post = findPostById(postId);
        Member member = memberRepository.getById(loginUser.memberId());
        post.validateAuthorization(member);
        return PostResponse.from(post);
    }

    public PostsResponse readAllByTripId(LoginUser loginUser, Long tripId) {
        Member member = memberRepository.getById(loginUser.memberId());
        findValidatedTripById(tripId, member);

        List<Post> posts = postRepository.findAllByTripId(tripId).stream()
                .sorted(Comparator.comparing(Post::pointRecordedAt).reversed())
                .collect(Collectors.toList());
        return PostsResponse.from(posts);
    }

    public void update(LoginUser loginUser, Long postId, PostUpdateRequest postUpdateRequest, MultipartFile file) {
        Post post = findPostById(postId);
        Member member = memberRepository.getById(loginUser.memberId());
        post.validateAuthorization(member);

        post.changeTitle(postUpdateRequest.title());
        post.changeWriting(postUpdateRequest.writing());
        updateFileOfPost(file, post);
    }

    public void delete(LoginUser loginUser, Long postId) {
        Post post = findPostById(postId);
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

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
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

