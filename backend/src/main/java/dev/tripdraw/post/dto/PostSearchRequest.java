package dev.tripdraw.post.dto;

public record PostSearchRequest(PostSearchConditions conditions, PostSearchPaging paging) {
}
