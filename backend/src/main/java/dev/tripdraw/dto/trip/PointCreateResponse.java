package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Point;
import io.swagger.v3.oas.annotations.media.Schema;

public record PointCreateResponse(
        @Schema(description = "위치 점 Id", example = "1")
        Long pointId
) {

    public static PointCreateResponse from(Point point) {
        return new PointCreateResponse(point.id());
    }
}
