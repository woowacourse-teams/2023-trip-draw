package dev.tripdraw.common;

import static dev.tripdraw.common.MdcToken.REQUEST_ID;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;

public class MdcFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        MDC.put(REQUEST_ID.key(), UUID.randomUUID().toString());
        chain.doFilter(request, response);

    }
}
