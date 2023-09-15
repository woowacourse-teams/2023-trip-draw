package dev.tripdraw.common.dto;

import dev.tripdraw.common.domain.Paging;

public record SearchPaging(Long lastViewedId, Integer limit) {
    public Paging toPaging() {
        return new Paging(lastViewedId, limit);
    }
}
