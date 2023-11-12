package dev.tripdraw.post.dto.v1;

import dev.tripdraw.post.dto.PostResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record PostsResponseV1(
        @Schema(description = "감상 목록")
        List<PostResponseV1> posts
) {
    public static PostsResponseV1 from(List<PostResponse> posts, Long loginUserId) {
        return new PostsResponseV1(posts.stream()
                .map(PostResponseV1::from)
                .toList());
    }
}
