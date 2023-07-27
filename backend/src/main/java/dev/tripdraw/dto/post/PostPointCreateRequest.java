package dev.tripdraw.dto.post;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.tripdraw.domain.trip.Point;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record PostPointCreateRequest(
        @NotNull
        Long tripId,
        @NotBlank @Size(max = 100)
        String title,
        @NotBlank
        String address,
        @Size(max = 10_000)
        String writing,
        @NotNull
        Double latitude,
        @NotNull
        Double longitude,
        @NotNull
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(description = "yyyy-MM-dd'T'HH:mm:ss 형식")
        LocalDateTime recordedAt
) {

    public Point toPoint() {
        return new Point(latitude, longitude, recordedAt);
    }
}
