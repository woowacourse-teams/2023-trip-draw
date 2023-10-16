package dev.tripdraw.area.dto;

import java.util.List;
import org.springframework.http.ResponseEntity;

public record AreaResponse(List<String> areas) {
    public static AreaResponse from(List<String> areas) {
        return new AreaResponse(areas);
    }
}
