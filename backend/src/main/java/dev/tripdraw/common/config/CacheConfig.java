package dev.tripdraw.common.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.tripdraw.common.cache.CacheType;
import java.util.Arrays;
import java.util.List;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches());
        return cacheManager;
    }

    private List<CaffeineCache> caches() {
        return Arrays.stream(CacheType.values())
                .map(cacheType -> new CaffeineCache(cacheType.cacheName(), cache(cacheType)))
                .toList();
    }

    private Cache<Object, Object> cache(CacheType cacheType) {
        return Caffeine.newBuilder()
                .maximumSize(cacheType.maximumSize())
                .build();
    }
}
