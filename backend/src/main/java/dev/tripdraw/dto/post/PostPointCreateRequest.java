package dev.tripdraw.dto.post;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.tripdraw.domain.trip.Point;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record PostPointCreateRequest(
        Long tripId,
        String title,
        String address,
        String writing,
        Double latitude,
        Double longitude,
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(description = "yyyy-MM-dd'T'HH:mm:ss 형식")
        LocalDateTime recordedAt
) {

    public Point toPoint() {
        return new Point(latitude, longitude, recordedAt);
    }
}
