package dev.tripdraw.admin.dto;

import dev.tripdraw.post.domain.Post;
import dev.tripdraw.trip.dto.PointResponse;
import java.util.Objects;

public record AdminPostResponse(
        Long postId,
        Long tripId,
        String title,
        String address,
        String writing,
        PointResponse pointResponse,
        String postImageUrl,
        String routeImageUrl
) {

    private static final String EMPTY_IMAGE_URL = "";

    public static AdminPostResponse from(Post post) {
        return new AdminPostResponse(
                post.id(),
                post.tripId(),
                post.title(),
                post.address(),
                post.writing(),
                PointResponse.from(post.point()),
                Objects.requireNonNullElse(post.postImageUrl(), EMPTY_IMAGE_URL),
                Objects.requireNonNullElse(post.routeImageUrl(), EMPTY_IMAGE_URL)
        );
    }
}
