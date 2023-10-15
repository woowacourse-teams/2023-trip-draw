package dev.tripdraw.common.async;

import static dev.tripdraw.common.log.MdcToken.REQUEST_ID;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final String LOG_FORMAT = "[%s] %s";

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        log.info(String.format(LOG_FORMAT, MDC.get(REQUEST_ID.key()), throwable.getMessage()), throwable);
    }
}
