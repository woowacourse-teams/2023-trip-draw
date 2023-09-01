package dev.tripdraw.common.log;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@RequiredArgsConstructor
public enum MdcToken {
    REQUEST_ID("request_id"),
    ;

    private final String key;
}
