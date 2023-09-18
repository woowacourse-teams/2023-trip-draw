package dev.tripdraw.common.async;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.task.execution.pool")
public record AsyncConfigurationProperties(
        int coreSize,
        int maxSize,
        int queueCapacity
) {
}
