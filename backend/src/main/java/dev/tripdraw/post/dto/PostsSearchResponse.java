package dev.tripdraw.post.dto;

import java.util.List;

public record PostsSearchResponse(List<PostSearchResponse> posts, boolean hasNextPage) {
    public static PostsSearchResponse of(List<PostSearchResponse> postSearchResponses, boolean hasNextPage) {
        return new PostsSearchResponse(postSearchResponses, hasNextPage);
    }
}
