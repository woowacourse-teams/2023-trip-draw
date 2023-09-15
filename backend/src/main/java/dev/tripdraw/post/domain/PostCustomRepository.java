package dev.tripdraw.post.domain;

import dev.tripdraw.common.domain.Paging;

import java.util.List;

public interface PostCustomRepository {

    List<Post> findAllByConditions(SearchConditions conditions, Paging paging);
}
