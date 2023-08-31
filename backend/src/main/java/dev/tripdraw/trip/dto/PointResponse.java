package dev.tripdraw.trip.dto;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.tripdraw.trip.domain.Point;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record PointResponse(
        @Schema(description = "위치 점 Id", example = "1")
        Long pointId,

        @Schema(description = "위도", example = "37.56663888630603")
        Double latitude,

        @Schema(description = "경도", example = "126.97838310403904")
        Double longitude,

        @Schema(description = "감상 존재 여부", example = "false")
        boolean hasPost,

        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(description = "yyyy-MM-dd'T'HH:mm:ss 형식", example = "2023-07-23T19:48:27", type = "string")
        LocalDateTime recordedAt
) {

    public static PointResponse from(Point point) {
        return new PointResponse(point.id(), point.latitude(), point.longitude(), point.hasPost(), point.recordedAt());
    }
}
