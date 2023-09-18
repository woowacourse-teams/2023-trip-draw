package dev.tripdraw.post.query;

import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.dto.PostSearchConditions;
import dev.tripdraw.post.dto.PostSearchPaging;

import java.util.List;

public interface PostCustomRepository {

    List<Post> findAllByConditions(PostSearchConditions conditions, PostSearchPaging paging);
}
