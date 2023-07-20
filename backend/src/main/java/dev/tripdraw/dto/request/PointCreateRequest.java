package dev.tripdraw.dto.request;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.tripdraw.domain.trip.Point;
import java.time.LocalDateTime;

public record PointCreateRequest(
        Long tripId,
        Double latitude,
        Double longitude,
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime recordedAt
) {

    public Point toPoint() {
        return new Point(latitude, longitude, recordedAt);
    }
}
