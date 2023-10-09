package dev.tripdraw.admin.application;

import dev.tripdraw.admin.dto.AdminPostResponse;
import dev.tripdraw.admin.dto.AdminTripResponse;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.trip.domain.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminService {

    private final PostRepository postRepository;
    private final TripRepository tripRepository;

    public AdminTripResponse findTripById(Long tripId) {
        return AdminTripResponse.from(tripRepository.getById(tripId));
    }

    public void deleteTrip(Long tripId) {
        tripRepository.deleteById(tripId);
    }

    public AdminPostResponse findPostById(Long postId) {
        return AdminPostResponse.from(postRepository.getByPostId(postId));
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }
}
