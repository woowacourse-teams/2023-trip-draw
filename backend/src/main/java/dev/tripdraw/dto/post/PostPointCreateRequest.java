package dev.tripdraw.dto.post;

import dev.tripdraw.domain.trip.Point;
import java.time.LocalDateTime;

public record PostPointCreateRequest(
        Long tripId,
        String title,
        String address,
        String writing,
        Double latitude,
        Double longitude,
        LocalDateTime recordedAt
) {

    public Point toPoint() {
        return new Point(latitude, longitude, recordedAt);
    }
}
