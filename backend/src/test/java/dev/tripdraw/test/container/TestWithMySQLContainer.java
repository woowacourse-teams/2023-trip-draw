package dev.tripdraw.test.container;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class TestWithMySQLContainer {

    private static final String USER_NAME = "root";
    private static final String PASSWORD = "test";
    private static final String DATABASE_NAME = "LOCAL";
    private static final String MYSQL_VERSION = "mysql:8.0";

    @Container
    protected static MySQLContainer container;

    static {
        container = new MySQLContainer(MYSQL_VERSION)
                .withDatabaseName(DATABASE_NAME)
                .withUsername(USER_NAME)
                .withPassword(PASSWORD);
    }

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", () -> USER_NAME);
        registry.add("spring.datasource.password", () -> PASSWORD);
    }
}
