package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Point;
import java.time.LocalDateTime;

public record PointResponse(Long pointId, Double latitude, Double longitude, LocalDateTime recordedAt) {

    public static PointResponse from(Point point) {
        return new PointResponse(point.id(), point.latitude(), point.longitude(), point.recordedAt());
    }
}
