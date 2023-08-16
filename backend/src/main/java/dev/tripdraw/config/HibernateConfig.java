package dev.tripdraw.config;

import static org.hibernate.cfg.AvailableSettings.STATEMENT_INSPECTOR;

import dev.tripdraw.common.QueryInspector;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class HibernateConfig {

    private final QueryInspector queryInspector;

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> hibernateProperties.put(STATEMENT_INSPECTOR, queryInspector);
    }
}
