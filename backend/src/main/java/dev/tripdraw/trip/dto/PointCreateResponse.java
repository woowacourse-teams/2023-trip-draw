package dev.tripdraw.trip.dto;

import dev.tripdraw.trip.domain.Point;
import io.swagger.v3.oas.annotations.media.Schema;

public record PointCreateResponse(
        @Schema(description = "위치 점 Id", example = "1")
        Long pointId
) {

    public static PointCreateResponse from(Point point) {
        return new PointCreateResponse(point.id());
    }
}
