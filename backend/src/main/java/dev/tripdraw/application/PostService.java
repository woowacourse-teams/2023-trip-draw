package dev.tripdraw.application;

import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.exception.trip.TripExceptionType.TRIP_NOT_FOUND;

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
import dev.tripdraw.exception.member.MemberException;
import dev.tripdraw.exception.trip.TripException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final TripRepository tripRepository;
    private final MemberRepository memberRepository;

    public PostService(
            PostRepository postRepository,
            TripRepository tripRepository,
            MemberRepository memberRepository
    ) {
        this.postRepository = postRepository;
        this.tripRepository = tripRepository;
        this.memberRepository = memberRepository;
    }

    public PostCreateResponse addAtCurrentPoint(
            LoginUser loginUser,
            PostAndPointCreateRequest postAndPointCreateRequest
    ) {
        Member member = findMemberByNickname(loginUser.nickname());
        Trip trip = findTripById(postAndPointCreateRequest.tripId());
        trip.validateAuthorization(member);

        Point point = postAndPointCreateRequest.toPoint();
        trip.add(point);
        tripRepository.flush();

        Post post = postAndPointCreateRequest.toPost(member, point);
        Post savedPost = postRepository.save(post);
        return PostCreateResponse.from(savedPost);
    }

    public PostCreateResponse addAtExistingLocation(LoginUser loginUser, PostRequest postRequest) {
        Member member = findMemberByNickname(loginUser.nickname());
        Trip trip = findTripById(postRequest.tripId());
        trip.validateAuthorization(member);

        Point point = trip.findPointById(postRequest.pointId());

        Post post = postRequest.toPost(member, point);
        Post savedPost = postRepository.save(post);
        return PostCreateResponse.from(savedPost);
    }

    private Trip findTripById(Long tripId) {
        return tripRepository.findById(tripId)
                .orElseThrow(() -> new TripException(TRIP_NOT_FOUND));
    }

    private Member findMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}

