package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Point;
import java.time.LocalDateTime;

public record PointCreateResponse(Long pointId, Double latitude, Double longitude, LocalDateTime recordedAt) {

    public static PointCreateResponse from(Point point) {
        return new PointCreateResponse(point.id(), point.latitude(), point.longitude(), point.recordedAt());
    }
}
