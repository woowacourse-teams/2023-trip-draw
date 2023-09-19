package dev.tripdraw.common.domain;

public record Paging(Long lastViewedId, Integer limit) {
    public boolean hasNextPage(int size) {
        return size > limit;
    }
}
