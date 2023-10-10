package dev.tripdraw.admin.application;

import dev.tripdraw.admin.dto.AdminPostResponse;
import dev.tripdraw.admin.dto.AdminPostsResponse;
import dev.tripdraw.admin.dto.AdminTripResponse;
import dev.tripdraw.admin.dto.AdminTripsResponse;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.post.dto.PostSearchPaging;
import dev.tripdraw.post.query.PostCustomRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.query.TripCustomRepository;
import dev.tripdraw.trip.query.TripPaging;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminService {

    private final PostRepository postRepository;
    private final TripRepository tripRepository;
    private final TripCustomRepository tripCustomRepository;
    private final PostCustomRepository postCustomRepository;

    @Transactional(readOnly = true)
    public AdminTripsResponse readTrips(Long lastViewId, Integer limit) {
        TripPaging tripPaging = new TripPaging(lastViewId, limit);
        List<Trip> trips = tripCustomRepository.findAll(tripPaging);
        if (limit < trips.size()) {
            return AdminTripsResponse.of(trips.subList(0, limit), true);
        }
        return AdminTripsResponse.of(trips, false);
    }

    @Transactional(readOnly = true)
    public AdminTripResponse readTripById(Long tripId) {
        return AdminTripResponse.from(tripRepository.getById(tripId));
    }

    public void deleteTrip(Long tripId) {
        tripRepository.deleteById(tripId);
    }

    @Transactional(readOnly = true)
    public AdminPostsResponse readPosts(Long lastViewId, Integer limit) {
        PostSearchPaging postSearchPaging = new PostSearchPaging(lastViewId, limit);
        List<Post> posts = postCustomRepository.findAll(postSearchPaging);
        if (limit < posts.size()) {
            return AdminPostsResponse.of(posts.subList(0, limit), true);
        }
        return AdminPostsResponse.of(posts, false);
    }

    @Transactional(readOnly = true)
    public AdminPostResponse readPostById(Long postId) {
        return AdminPostResponse.from(postRepository.getByPostId(postId));
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
}
