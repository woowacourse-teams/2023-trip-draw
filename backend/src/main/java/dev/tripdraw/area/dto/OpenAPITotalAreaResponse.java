package dev.tripdraw.area.dto;

import dev.tripdraw.area.application.OpenAPIAreaResponse;
import java.util.List;

public record OpenAPITotalAreaResponse(String id, List<OpenAPIAreaResponse> result) {
}
