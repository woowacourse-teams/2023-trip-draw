package dev.tripdraw.post.dto;

import dev.tripdraw.common.dto.SearchPaging;

public record PostSearchRequest(PostSearchConditions condition, SearchPaging paging) {
}
