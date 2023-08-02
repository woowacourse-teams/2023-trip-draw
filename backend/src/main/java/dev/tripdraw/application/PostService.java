package dev.tripdraw.application;

import static dev.tripdraw.application.file.FileType.POST_IMAGE;
import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.exception.post.PostExceptionType.POST_NOT_FOUNT;
import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_NOT_FOUND;

import dev.tripdraw.application.file.FileUploader;
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
import dev.tripdraw.exception.member.MemberException;
import dev.tripdraw.exception.post.PostException;
import dev.tripdraw.exception.trip.TripException;
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

    public PostService(
            PostRepository postRepository,
            TripRepository tripRepository,
            MemberRepository memberRepository,
            FileUploader fileUploader
    ) {
        this.postRepository = postRepository;
        this.tripRepository = tripRepository;
        this.memberRepository = memberRepository;
        this.fileUploader = fileUploader;
    }

    public PostCreateResponse addAtCurrentPoint(
            LoginUser loginUser,
            PostAndPointCreateRequest postAndPointCreateRequest,
            MultipartFile file
    ) {
        Member member = findMemberByNickname(loginUser.nickname());
        Trip trip = findTripById(postAndPointCreateRequest.tripId());
        trip.validateAuthorization(member);

        Point point = postAndPointCreateRequest.toPoint();
        trip.add(point);
        tripRepository.flush();

        Post post = postAndPointCreateRequest.toPost(member, point);
        Post savedPost = savePostWithImageUrl(file, post);
        return PostCreateResponse.from(savedPost);
    }

    public PostCreateResponse addAtExistingLocation(
            LoginUser loginUser,
            PostRequest postRequest,
            MultipartFile file
    ) {
        Member member = findMemberByNickname(loginUser.nickname());
        Trip trip = findTripById(postRequest.tripId());
        trip.validateAuthorization(member);

        Point point = trip.findPointById(postRequest.pointId());

        Post post = postRequest.toPost(member, point);
        Post savedPost = savePostWithImageUrl(file, post);
        return PostCreateResponse.from(savedPost);
    }

    public PostResponse read(LoginUser loginUser, Long postId) {
        dev.tripdraw.domain.post.Post post = findPostById(postId);
        Member member = findMemberByNickname(loginUser.nickname());
        post.validateAuthorization(member);
        return PostResponse.from(post);
    }

    private Trip findTripById(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new TripException(TRIP_NOT_FOUND));
    }

    private Member findMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    private Post savePostWithImageUrl(MultipartFile file, Post post) {
        String imageUrl = fileUploader.upload(file, POST_IMAGE);
        post.setImageUrl(imageUrl);

        Post savedPost = postRepository.save(post);
        return savedPost;
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUNT));
    }
}
