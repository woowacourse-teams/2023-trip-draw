package dev.tripdraw.dto.post;

import dev.tripdraw.domain.post.Post;
import dev.tripdraw.dto.trip.PointResponse;

public record PostResponse(
        Long postId,
        Long tripId,
        String title,
        String address,
        String writing,
        PointResponse pointResponse
) {

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.id(),
                post.tripId(),
                post.title(),
                post.address(),
                post.writing(),
                PointResponse.from(post.point())
        );
    }
}
