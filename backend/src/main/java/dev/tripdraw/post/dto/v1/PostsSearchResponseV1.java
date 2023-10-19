package dev.tripdraw.post.dto.v1;

import dev.tripdraw.post.dto.PostSearchResponse;
import java.util.List;

public record PostsSearchResponseV1(List<PostSearchResponseV1> posts, boolean hasNextPage) {
    public static PostsSearchResponseV1 of(List<PostSearchResponse> postSearchResponses, boolean hasNextPage) {
        List<PostSearchResponseV1> responseV1s = postSearchResponses.stream()
                .map(PostSearchResponseV1::from)
                .toList();
        return new PostsSearchResponseV1(responseV1s, hasNextPage);
    }
}
