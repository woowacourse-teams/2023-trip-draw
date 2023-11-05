package dev.tripdraw.area.dto;

import java.util.List;

public record FullAreaResponses(List<FullAreaResponse> areas) {

    public static FullAreaResponses from(List<FullAreaResponse> areas) {
        return new FullAreaResponses(areas);
    }
}
