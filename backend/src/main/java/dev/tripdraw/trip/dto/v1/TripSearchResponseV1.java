package dev.tripdraw.trip.dto.v1;

import dev.tripdraw.trip.dto.TripSearchResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Objects;

public record TripSearchResponseV1(
        @Schema(description = "여행 Id", example = "1")
        Long tripId,

        @Schema(description = "여행명", example = "통후추의 여행")
        String name,

        @Schema(description = "이미지 주소", example = "https://tripdraw.site/post-images/cd678ca2-30d5-11ee-be56-0242ac120002.jpg")
        String imageUrl,

        @Schema(description = "경로 이미지 주소", example = "https://tripdraw.site/route-images/cd678ca2-30d5-11ee-be56-0242ac120002.png")
        String routeImageUrl,

        @Schema(description = "여행 시작 시각", example = "2023-07-23T19:48:27")
        LocalDateTime startTime,

        @Schema(description = "여행 종료 시각", example = "2023-07-23T19:48:27")
        LocalDateTime endTime
) {

    private static final String EMPTY_IMAGE_URL = "";

    public static TripSearchResponseV1 from(TripSearchResponse trip) {
        return new TripSearchResponseV1(
                trip.tripId(),
                trip.name(),
                Objects.requireNonNullElse(trip.imageUrl(), EMPTY_IMAGE_URL),
                Objects.requireNonNullElse(trip.routeImageUrl(), EMPTY_IMAGE_URL),
                trip.startTime(),
                trip.endTime()
        );
    }
}
