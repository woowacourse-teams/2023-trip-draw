package dev.tripdraw.common.config;

import static org.springframework.util.unit.DataUnit.MEGABYTES;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class MultipartConfig {

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory multipartConfigFactory = new MultipartConfigFactory();
        multipartConfigFactory.setMaxRequestSize(DataSize.of(20, MEGABYTES));
        multipartConfigFactory.setMaxFileSize(DataSize.of(20, MEGABYTES));

        return multipartConfigFactory.createMultipartConfig();
    }
}
