package dev.tripdraw.common;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@RequiredArgsConstructor
@Component
public class QueryInspector implements StatementInspector {

    private final QueryCounter queryCounter;

    @Override
    public String inspect(String sql) {
        if (isInRequestScope()) {
            queryCounter.increase();
        }
        return sql;
    }

    private boolean isInRequestScope() {
        return Objects.nonNull(RequestContextHolder.getRequestAttributes());
    }
}
