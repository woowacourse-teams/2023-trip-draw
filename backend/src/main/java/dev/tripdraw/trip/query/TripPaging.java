package dev.tripdraw.trip.query;

public record TripPaging(Long lastViewedId, Integer limit) {
    private static final int LIMIT_MAXIMUM = 100;

    public TripPaging(Long lastViewedId, Integer limit) {
        this.lastViewedId = lastViewedId;
        this.limit = ceil(limit);
    }

    private int ceil(Integer limit) {
        return Math.min(limit, LIMIT_MAXIMUM);
    }

    public boolean hasNextPage(int size) {
        return limit < size;
    }
}
