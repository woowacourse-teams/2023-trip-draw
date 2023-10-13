package dev.tripdraw.test;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class DatabaseCleaner extends AbstractTestExecutionListener {

    private static final String TRUNCATE_TABLE_QUERY = """
            SELECT Concat('TRUNCATE TABLE \"', TABLE_NAME, '\";') 
            FROM INFORMATION_SCHEMA.TABLES
            WHERE TABLE_SCHEMA = 'PUBLIC'
            """;

    @Override
    public void afterTestMethod(TestContext testContext) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(testContext);
        List<String> truncateTableQueries = getTruncateTableQueries(jdbcTemplate);
        truncateTables(jdbcTemplate, truncateTableQueries);
    }

    private JdbcTemplate getJdbcTemplate(TestContext testContext) {
        return testContext.getApplicationContext().getBean(JdbcTemplate.class);
    }

    private List<String> getTruncateTableQueries(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList(TRUNCATE_TABLE_QUERY, String.class);
    }

    private void truncateTables(JdbcTemplate jdbcTemplate, List<String> truncateTableQueries) {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        truncateTableQueries.forEach(jdbcTemplate::execute);
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
