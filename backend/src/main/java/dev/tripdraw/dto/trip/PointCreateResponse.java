package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Point;

public record PointCreateResponse(Long id) {

    public static PointCreateResponse from(Point point) {
        return new PointCreateResponse(point.id());
    }
}
