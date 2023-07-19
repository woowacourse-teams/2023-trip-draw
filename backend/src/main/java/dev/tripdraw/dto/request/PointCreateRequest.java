package dev.tripdraw.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import dev.tripdraw.domain.trip.Point;
import java.time.LocalDateTime;

public record PointCreateRequest(
        Long tripId,
        Double latitude,
        Double longitude,
        @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime recordedAt
) {

    public Point toPoint() {
        return new Point(latitude, longitude, recordedAt);
    }
}
