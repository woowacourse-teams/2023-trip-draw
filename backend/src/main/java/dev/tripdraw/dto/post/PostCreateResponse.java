package dev.tripdraw.dto.post;

import dev.tripdraw.domain.post.Post;
import io.swagger.v3.oas.annotations.media.Schema;

public record PostCreateResponse(
        @Schema(description = "감상 Id", example = "1")
        Long postId
) {

    public static PostCreateResponse from(Post post) {
        return new PostCreateResponse(post.id());
    }
}
