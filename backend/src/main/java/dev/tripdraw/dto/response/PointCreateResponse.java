package dev.tripdraw.dto.response;

import dev.tripdraw.domain.trip.Point;

public record PointCreateResponse(Long id) {

    public static PointCreateResponse from(Point point) {
        return new PointCreateResponse(point.getId());
    }
}
