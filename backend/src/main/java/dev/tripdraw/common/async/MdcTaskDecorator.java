package dev.tripdraw.common.async;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(final Runnable runnable) {
        Map<String, String> threadContext = MDC.getCopyOfContextMap();
        return () -> {
            MDC.setContextMap(threadContext);
            runnable.run();
        };
    }
}
