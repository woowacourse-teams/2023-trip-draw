package dev.tripdraw.trip.dto;

import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;

public record TripResponse(
        @Schema(description = "여행 Id", example = "1")
        Long tripId,

        @Schema(description = "나의 여행 확인 flag", example = "true")
        boolean isMine,

        @Schema(description = "작성자 닉네임", example = "통후추")
        String authorNickname,

        @Schema(description = "여행명", example = "통후추의 여행")
        String name,

        @Schema(description = "경로")
        List<PointResponse> route,

        @Schema(description = "여행 상태 (option : ONGOING, FINISHED)", example = "ONGOING")
        TripStatus status,

        @Schema(description = "Base64로 인코딩 된 여행 대표 이미지 URL", example = "https://tripdraw.site/post-image/AWF242FWF42.jpg")
        String imageUrl,

        @Schema(description = "Base64로 인코딩 된 여행 대표 이미지 URL", example = "https://tripdraw.site/post-image/AWF242FWF42.jpg")
        String routeImageUrl
) {

    private static final String EMPTY_IMAGE_URL = "";

    public static TripResponse from(Trip trip, Long loginUserId) {
        return new TripResponse(
                trip.id(),
                trip.member().id().equals(loginUserId),
                trip.member().nickname(),
                trip.nameValue(),
                generateRoute(trip),
                trip.status(),
                Objects.requireNonNullElse(trip.imageUrl(), EMPTY_IMAGE_URL),
                Objects.requireNonNullElse(trip.routeImageUrl(), EMPTY_IMAGE_URL)
        );
    }

    private static List<PointResponse> generateRoute(Trip trip) {
        return trip.points().stream()
                .map(PointResponse::from)
                .toList();
    }
}
