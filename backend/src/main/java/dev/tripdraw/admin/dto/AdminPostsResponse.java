package dev.tripdraw.admin.dto;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import dev.tripdraw.post.domain.Post;
import java.util.List;

public record AdminPostsResponse(
        List<AdminPostResponse> items,
        boolean hasNextPage
) {
    public static AdminPostsResponse of(List<Post> posts, boolean hasNextPage) {
        return posts.stream()
                .map(AdminPostResponse::from)
                .collect(collectingAndThen(toList(), items -> new AdminPostsResponse(items, hasNextPage)));
    }
}
