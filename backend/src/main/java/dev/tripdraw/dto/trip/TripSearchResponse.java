package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Trip;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

public record TripSearchResponse(
        @Schema(description = "여행 Id", example = "1")
        Long tripId,

        @Schema(description = "여행명", example = "통후추의 여행")
        String name,

        @Schema(description = "이미지 주소", example = "https://tripdraw.site/post-images/cd678ca2-30d5-11ee-be56-0242ac120002.jpg")
        String imageUrl,

        @Schema(description = "경로 이미지 주소", example = "https://tripdraw.site/route-images/cd678ca2-30d5-11ee-be56-0242ac120002.png")
        String routeImageUrl
) {

    private static final String EMPTY_IMAGE_URL = "";

    public static TripSearchResponse from(Trip trip) {
        return new TripSearchResponse(
                trip.id(),
                trip.nameValue(),
                Objects.requireNonNullElse(trip.imageUrl(), EMPTY_IMAGE_URL),
                Objects.requireNonNullElse(trip.imageUrl(), EMPTY_IMAGE_URL)
        );
    }
}
