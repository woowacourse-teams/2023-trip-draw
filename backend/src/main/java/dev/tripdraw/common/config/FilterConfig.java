package dev.tripdraw.common.config;

import dev.tripdraw.common.log.LogFilter;
import dev.tripdraw.common.log.MdcFilter;
import dev.tripdraw.common.log.QueryCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

    private static final int FIRST = 0;
    private static final int SECOND = 1;

    private final QueryCounter queryCounter;

    @Bean
    FilterRegistrationBean<MdcFilter> mdcFilter() {
        MdcFilter mdcFilter = new MdcFilter();
        FilterRegistrationBean<MdcFilter> registrationBean = new FilterRegistrationBean<>(mdcFilter);
        registrationBean.setOrder(FIRST);
        return registrationBean;
    }

    @Bean
    FilterRegistrationBean<LogFilter> logFilter() {
        LogFilter logFilter = new LogFilter(queryCounter);
        FilterRegistrationBean<LogFilter> registrationBean = new FilterRegistrationBean<>(logFilter);
        registrationBean.setOrder(SECOND);
        return registrationBean;
    }
}
