package dev.tripdraw.post.dto;

import dev.tripdraw.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record PostsResponse(
        @Schema(description = "감상 목록")
        List<PostResponse> posts
) {
    public static PostsResponse from(List<Post> posts) {
        return new PostsResponse(posts.stream()
                .map(PostResponse::from)
                .toList());
    }
}
