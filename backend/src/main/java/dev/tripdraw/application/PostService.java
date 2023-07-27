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
import dev.tripdraw.dto.post.PostPointCreateRequest;
import dev.tripdraw.dto.post.PostResponse;
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

    public PostResponse createOfCurrentLocation(LoginUser loginUser, PostPointCreateRequest postPointCreateRequest) {
        // Trip 조회하고 loginUser의 소유가 맞는지 확인
        Member member = findMemberByNickname(loginUser.nickname());
        Long tripId = postPointCreateRequest.tripId();
        Trip trip = findTripById(tripId);
        trip.validateAuthorization(member);
        // Point 생성 -> 연관관계 : Trip에 add 해주기 -> TripRepository save -> 저장 확인 필요
        Point point = postPointCreateRequest.toPoint();
        trip.add(point);
        tripRepository.flush();
        // Post 생성 -> 연관관계 : Point 넣어주면 됨 -> PostRepository save -> 저장 확인 필요
        Post post = createPost(postPointCreateRequest, member, point);
        Post savedPost = postRepository.save(post);
        // 반환
        return PostResponse.from(savedPost);
    }

    private Post createPost(PostPointCreateRequest postPointCreateRequest, Member member, Point point) {
        Post post = new Post(
                postPointCreateRequest.title(),
                point,
                postPointCreateRequest.address(),
                postPointCreateRequest.writing(),
                member,
                postPointCreateRequest.tripId()
        );
        return post;
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

