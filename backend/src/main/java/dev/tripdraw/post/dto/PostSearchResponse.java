package dev.tripdraw.post.dto;

import dev.tripdraw.post.domain.Post;

import java.time.LocalDateTime;

public record PostSearchResponse(
        Long postId,
        Long tripId,
        String title,
        String address,
        String writing,
        String postImageUrl,
        String routeImageUrl,
        LocalDateTime recordedAt
) {
    public static PostSearchResponse from(Post post) {
        return new PostSearchResponse(
                post.id(),
                post.tripId(),
                post.title(),
                post.address(),
                post.writing(),
                post.postImageUrl(),
                post.routeImageUrl(),
                post.pointRecordedAt()
        );
    }
}
