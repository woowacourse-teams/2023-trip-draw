package dev.tripdraw.application;

import static dev.tripdraw.domain.file.FileType.POST_IMAGE;
import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.exception.post.PostExceptionType.POST_NOT_FOUNT;
import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_NOT_FOUND;

import dev.tripdraw.application.draw.RouteImageGenerator;
import dev.tripdraw.application.file.FileUploader;
import dev.tripdraw.domain.file.FileType;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.post.Post;
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
import dev.tripdraw.exception.member.MemberException;
import dev.tripdraw.exception.post.PostException;
import dev.tripdraw.exception.trip.TripException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final TripRepository tripRepository;
    private final MemberRepository memberRepository;
    private final FileUploader fileUploader;
    private final RouteImageGenerator routeImageGenerator;

    public PostService(
            PostRepository postRepository,
            TripRepository tripRepository,
            MemberRepository memberRepository,
            FileUploader fileUploader,
            RouteImageGenerator routeImageGenerator
    ) {
        this.postRepository = postRepository;
        this.tripRepository = tripRepository;
        this.memberRepository = memberRepository;
        this.fileUploader = fileUploader;
        this.routeImageGenerator = routeImageGenerator;
    }

    public PostCreateResponse addAtCurrentPoint(
            LoginUser loginUser,
            PostAndPointCreateRequest postAndPointCreateRequest,
            MultipartFile file
    ) {
        Member member = findMemberByNickname(loginUser.nickname());
        Trip trip = findValidatedTripById(postAndPointCreateRequest.tripId(), member);

        Point point = postAndPointCreateRequest.toPoint();
        trip.add(point);
        tripRepository.flush();

        Post post = postAndPointCreateRequest.toPost(member, point);
        Post savedPost = postRepository.save(registerFileToPost(file, post));

        String routeImageName = generateRouteImage(trip, point);
        savedPost.changeRouteImageUrl(routeImageName);

        return PostCreateResponse.from(savedPost);
    }

    private String generateRouteImage(Trip trip, Point point) {
        return routeImageGenerator.generate(
                trip.getLatitudes(),
                trip.getLongitudes(),
                List.of(point.latitude()),
                List.of(point.longitude())
        );
    }

    public PostCreateResponse addAtExistingLocation(
            LoginUser loginUser,
            PostRequest postRequest,
            MultipartFile file
    ) {
        Member member = findMemberByNickname(loginUser.nickname());
        Trip trip = findValidatedTripById(postRequest.tripId(), member);

        Point point = trip.findPointById(postRequest.pointId());

        Post post = postRequest.toPost(member, point);
        Post savedPost = postRepository.save(registerFileToPost(file, post));

        String routeImageName = generateRouteImage(trip, point);
        savedPost.changeRouteImageUrl(routeImageName);

        return PostCreateResponse.from(savedPost);
    }

    public PostResponse read(LoginUser loginUser, Long postId) {
        Post post = findPostById(postId);
        Member member = findMemberByNickname(loginUser.nickname());
        post.validateAuthorization(member);
        return PostResponse.from(post);
    }

    public PostsResponse readAllByTripId(LoginUser loginUser, Long tripId) {
        Member member = findMemberByNickname(loginUser.nickname());
        findValidatedTripById(tripId, member);

        List<Post> posts = postRepository.findAllByTripId(tripId);
        return PostsResponse.from(posts);
    }

    public void update(LoginUser loginUser, Long postId, PostUpdateRequest postUpdateRequest, MultipartFile file) {
        Post post = findPostById(postId);
        Member member = findMemberByNickname(loginUser.nickname());
        post.validateAuthorization(member);

        post.changeTitle(postUpdateRequest.title());
        post.changeWriting(postUpdateRequest.writing());
        updateFileOfPost(file, post);
    }

    public void delete(LoginUser loginUser, Long postId) {
        Post post = findPostById(postId);
        Member member = findMemberByNickname(loginUser.nickname());
        post.validateAuthorization(member);

        postRepository.deleteById(postId);
    }

    private void updateFileOfPost(MultipartFile file, Post post) {
        if (file == null) {
            post.removePostImageUrl();
            return;
        }
        String imageUrl = fileUploader.upload(file, POST_IMAGE);
        post.changePostImageUrl(imageUrl);
    }

    private Trip findValidatedTripById(Long tripId, Member member) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripException(TRIP_NOT_FOUND));
        trip.validateAuthorization(member);
        return trip;
    }

    private Member findMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUNT));
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

