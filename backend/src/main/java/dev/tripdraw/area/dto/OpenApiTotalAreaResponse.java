package dev.tripdraw.area.dto;

import java.util.List;

public record OpenApiTotalAreaResponse(String id, List<OpenApiAreaResponse> result) {
}
