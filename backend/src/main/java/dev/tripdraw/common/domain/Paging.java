package dev.tripdraw.common.domain;

public record Paging(Long lastViewedId, Integer limit) {
    private static final int LIMIT_MAXIMUM = 100;

    public Paging(Long lastViewedId, Integer limit) {
        this.lastViewedId = lastViewedId;
        this.limit = ceil(limit);
    }

    private int ceil(Integer limit) {
        return Math.min(limit, LIMIT_MAXIMUM);
    }

    public boolean hasNextPage(int size) {
        return size > limit;
    }
}
