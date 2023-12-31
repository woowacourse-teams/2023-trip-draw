package dev.tripdraw.admin.application;

import dev.tripdraw.admin.dto.AdminPostResponse;
import dev.tripdraw.admin.dto.AdminPostsResponse;
import dev.tripdraw.admin.dto.AdminStatsResponse;
import dev.tripdraw.admin.dto.AdminTripResponse;
import dev.tripdraw.admin.dto.AdminTripsResponse;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostDynamicQueryRepository;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.post.dto.PostPaging;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripDynamicQueryRepository;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.dto.TripPaging;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminService {

    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;
    private final PostRepository postRepository;
    private final TripRepository tripRepository;
    private final TripDynamicQueryRepository tripDynamicQueryRepository;
    private final PostDynamicQueryRepository postDynamicQueryRepository;

    @Transactional(readOnly = true)
    public AdminTripsResponse readTrips(Long lastViewId, Integer limit) {
        TripPaging tripPaging = new TripPaging(lastViewId, limit);
        List<Trip> trips = tripDynamicQueryRepository.findAll(tripPaging);
        if (limit < trips.size()) {
            return AdminTripsResponse.of(trips.subList(0, limit), true);
        }
        return AdminTripsResponse.of(trips, false);
    }

    @Transactional(readOnly = true)
    public AdminTripResponse readTrip(Long tripId) {
        return AdminTripResponse.from(tripRepository.getTripWithPointsAndMemberByTripId(tripId));
    }

    public void deleteTrip(Long tripId) {
        tripRepository.deleteById(tripId);
    }

    @Transactional(readOnly = true)
    public AdminPostsResponse readPosts(Long lastViewId, Integer limit) {
        PostPaging postPaging = new PostPaging(lastViewId, limit);
        List<Post> posts = postDynamicQueryRepository.findAll(postPaging);
        if (limit < posts.size()) {
            return AdminPostsResponse.of(posts.subList(0, limit), true);
        }
        return AdminPostsResponse.of(posts, false);
    }

    @Transactional(readOnly = true)
    public AdminPostResponse readPost(Long postId) {
        return AdminPostResponse.from(postRepository.getPostWithPointAndMemberById(postId));
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Transactional(readOnly = true)
    public AdminStatsResponse stats() {
        return new AdminStatsResponse(
                memberRepository.count(),
                tripRepository.count(),
                pointRepository.count(),
                postRepository.count()
        );
    }
}
