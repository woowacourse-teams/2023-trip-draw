package dev.tripdraw.area.dto;

import java.util.List;

public record OpenAPITotalAreaResponse(String id, List<OpenAPIAreaResponse> result) {
}
