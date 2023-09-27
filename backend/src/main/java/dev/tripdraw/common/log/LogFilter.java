package dev.tripdraw.common.log;

import static dev.tripdraw.common.log.MdcToken.REQUEST_ID;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
@RequiredArgsConstructor
public class LogFilter implements Filter {

    private final static String LOG_FORMAT = "[{}], uri: {}, method: {}, time: {}ms, queryCount: {}";

    private final QueryCounter queryCounter;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        queryCounter.init();

        long start = System.currentTimeMillis();

        chain.doFilter(request, response);

        long end = System.currentTimeMillis();
        log(httpServletRequest.getRequestURI(), httpServletRequest.getMethod(), end - start);
        queryCounter.close();
    }

    private void log(String uri, String method, long time) {
        if (queryCounter.isOverWarningCount()) {
            log.warn(LOG_FORMAT, MDC.get(REQUEST_ID.key()), uri, method, time, queryCounter.count());
            return;
        }
        log.info(LOG_FORMAT, MDC.get(REQUEST_ID.key()), uri, method, time, queryCounter.count());
    }
}
