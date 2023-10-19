package dev.tripdraw.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataCleaner {

    private static final int OFF = 0;
    private static final int ON = 1;
    private static final int FIRST_COLUMN = 1;
    private static final String FLYWAY = "flyway";

    private final List<String> truncateQueries = new ArrayList<>();

    @Autowired
    private DataSource dataSource;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void clean() {
        if (truncateQueries.isEmpty()) {
            init();
        }
        setForeignKeyEnabled(OFF);
        truncateAllTables();
        setForeignKeyEnabled(ON);
    }

    private void setForeignKeyEnabled(final int enabled) {
        em.createNativeQuery("SET foreign_key_checks = " + enabled).executeUpdate();
    }

    private void truncateAllTables() {
        truncateQueries.stream()
                .map(em::createNativeQuery)
                .forEach(Query::executeUpdate);
    }

    private void init() {
        try (final Statement statement = dataSource.getConnection().createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SHOW TABLES ");

            while (resultSet.next()) {
                final String tableName = resultSet.getString(FIRST_COLUMN);
                if (tableName.contains(FLYWAY)) {
                    continue;
                }
                truncateQueries.add("TRUNCATE " + tableName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
