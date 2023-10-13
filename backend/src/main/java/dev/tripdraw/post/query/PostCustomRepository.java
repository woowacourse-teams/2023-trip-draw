package dev.tripdraw.post.query;

import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.dto.PostPaging;
import dev.tripdraw.post.dto.PostSearchConditions;
import java.util.List;

public interface PostCustomRepository {

    List<Post> findAllByConditions(PostSearchConditions conditions, PostPaging paging);

    List<Post> findAll(PostPaging paging);
}
