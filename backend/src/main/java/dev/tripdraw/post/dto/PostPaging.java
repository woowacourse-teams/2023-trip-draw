package dev.tripdraw.post.dto;

import java.util.Objects;

public record PostPaging(Long lastViewedId, Integer limit) {

    private static final int DEFAULT_LIMIT = 20;
    private static final int LIMIT_MAXIMUM = 100;

    public PostPaging(Long lastViewedId, Integer limit) {
        this.lastViewedId = lastViewedId;
        this.limit = preprocess(limit);
    }

    private int preprocess(Integer limit) {
        if (Objects.isNull(limit)) {
            return DEFAULT_LIMIT;
        }
        return ceil(limit);
    }

    private int ceil(Integer limit) {
        return Math.min(limit, LIMIT_MAXIMUM);
    }

    public boolean hasNextPage(int size) {
        return limit < size;
    }
}
