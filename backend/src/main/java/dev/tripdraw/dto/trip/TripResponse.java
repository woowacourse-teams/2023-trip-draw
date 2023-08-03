package dev.tripdraw.dto.trip;

import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record TripResponse(
        @Schema(description = "여행 Id", example = "1")
        Long tripId,

        @Schema(description = "여행명", example = "통후추의 여행")
        String name,

        @Schema(description = "경로")
        List<PointResponse> routes,

        @Schema(description = "여행 상태 (option : ONGOING, FINISHED)", example = "ONGOING")
        TripStatus status,

        @Schema(description = "Base64로 인코딩 된 여행 대표 이미지 URL", example = "https://tripdraw.site/post-image/AWF242FWF42.jpg")
        String imageUrl,

        @Schema(description = "Base64로 인코딩 된 여행 대표 이미지 URL", example = "https://tripdraw.site/post-image/AWF242FWF42.jpg")
        String routeImageUrl
) {

    public static TripResponse from(Trip trip) {
        return new TripResponse(
                trip.id(),
                trip.nameValue(),
                generateRoutes(trip),
                trip.status(),
                trip.imageUrl(),
                trip.routeImageUrl()
        );
    }

    private static List<PointResponse> generateRoutes(Trip trip) {
        return trip.points().stream()
                .map(PointResponse::from)
                .toList();
    }
}
