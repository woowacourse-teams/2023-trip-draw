package dev.tripdraw.post.dto;

import dev.tripdraw.post.dto.query.PostSearchConditions;
import dev.tripdraw.post.dto.query.PostSearchPaging;

public record PostSearchRequest(PostSearchConditions conditions, PostSearchPaging paging) {
}
