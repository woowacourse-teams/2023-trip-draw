package dev.tripdraw.post.dto.v1;

import dev.tripdraw.post.dto.PostSearchResponse;
import java.time.LocalDateTime;
import java.util.Objects;

public record PostSearchResponseV1(
        Long postId,
        Long tripId,
        String title,
        String address,
        String writing,
        String postImageUrl,
        String routeImageUrl,
        LocalDateTime recordedAt
) {

    private static final String EMPTY_IMAGE_URL = "";

    public static PostSearchResponseV1 from(PostSearchResponse post) {
        return new PostSearchResponseV1(
                post.postId(),
                post.tripId(),
                post.title(),
                post.address(),
                post.writing(),
                Objects.requireNonNullElse(post.postImageUrl(), EMPTY_IMAGE_URL),
                Objects.requireNonNullElse(post.routeImageUrl(), EMPTY_IMAGE_URL),
                post.recordedAt()
        );
    }
}
