package dev.tripdraw.common.cache;

public enum CacheType {

    AREAS("areas", 1);

    private final String cacheName;
    private final int maximumSize;

    CacheType(String cacheName, int maximumSize) {
        this.cacheName = cacheName;
        this.maximumSize = maximumSize;
    }

    public String cacheName() {
        return cacheName;
    }

    public int maximumSize() {
        return maximumSize;
    }
}
