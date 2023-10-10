package dev.tripdraw.trip.dto;

import dev.tripdraw.trip.domain.Trip;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Objects;

public record TripSearchResponse(
        @Schema(description = "여행 Id", example = "1")
        Long tripId,

        @Schema(description = "회원 닉네임", example = "통후추")
        String memberNickname,

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

    public static TripSearchResponse from(Trip trip, String memberNickname) {
        return new TripSearchResponse(
                trip.id(),
                memberNickname,
                trip.nameValue(),
                Objects.requireNonNullElse(trip.imageUrl(), EMPTY_IMAGE_URL),
                Objects.requireNonNullElse(trip.routeImageUrl(), EMPTY_IMAGE_URL),
                trip.createdAt(),
                trip.updatedAt()
        );
    }
}
