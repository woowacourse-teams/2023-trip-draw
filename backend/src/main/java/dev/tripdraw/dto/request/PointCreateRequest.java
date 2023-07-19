package dev.tripdraw.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import dev.tripdraw.domain.trip.Point;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record PointCreateRequest(
        Long tripId,
        Double latitude,
        Double longitude,
        @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(description = "yyyy-MM-dd'T'HH:mm:ss 형식") LocalDateTime recordedAt
) {

    public Point toPoint() {
        return Point.builder()
                .latitude(latitude)
                .longitude(longitude)
                .recordedAt(recordedAt)
                .build();
    }
}
