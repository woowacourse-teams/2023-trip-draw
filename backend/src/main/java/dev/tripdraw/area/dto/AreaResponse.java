package dev.tripdraw.area.dto;

import java.util.List;

public record AreaResponse(List<String> areas) {
    public static AreaResponse from(List<String> areas) {
        return new AreaResponse(areas);
    }
}
