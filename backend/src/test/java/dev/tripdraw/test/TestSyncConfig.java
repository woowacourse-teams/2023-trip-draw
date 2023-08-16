package dev.tripdraw.test;

import java.util.concurrent.Executor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;

@TestConfiguration
public class TestSyncConfig {

    @Bean
    public Executor taskExecutor() {
        return new SyncTaskExecutor();
    }
}
