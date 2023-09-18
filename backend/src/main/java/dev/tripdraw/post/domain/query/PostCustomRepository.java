package dev.tripdraw.post.domain.query;

import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.dto.query.PostSearchConditions;
import dev.tripdraw.post.dto.query.PostSearchPaging;

import java.util.List;

public interface PostCustomRepository {

    List<Post> findAllByConditions(PostSearchConditions conditions, PostSearchPaging paging);
}
