package dev.tripdraw.post.dto;

import dev.tripdraw.post.domain.Post;
import java.time.LocalDateTime;
import java.util.Objects;

public record PostSearchResponse(
        Long postId,
        Long tripId,
        boolean isMine,
        String authorNickname,
        String title,
        String address,
        String writing,
        String postImageUrl,
        String routeImageUrl,
        LocalDateTime recordedAt
) {

    private static final String EMPTY_IMAGE_URL = "";

    public static PostSearchResponse from(Post post, Long loginUserId) {
        return new PostSearchResponse(
                post.id(),
                post.tripId(),
                post.member().id().equals(loginUserId),
                post.member().nickname(),
                post.title(),
                post.address(),
                post.writing(),
                Objects.requireNonNullElse(post.postImageUrl(), EMPTY_IMAGE_URL),
                Objects.requireNonNullElse(post.routeImageUrl(), EMPTY_IMAGE_URL),
                post.pointRecordedAt()
        );
    }
}
