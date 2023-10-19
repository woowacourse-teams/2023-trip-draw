package dev.tripdraw.trip.dto.v1;

import dev.tripdraw.trip.domain.TripStatus;
import dev.tripdraw.trip.dto.PointResponse;
import dev.tripdraw.trip.dto.TripResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Objects;

public record TripResponseV1(
        @Schema(description = "여행 Id", example = "1")
        Long tripId,

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

    public static TripResponseV1 from(TripResponse trip) {
        return new TripResponseV1(
                trip.tripId(),
                trip.name(),
                trip.route(),
                trip.status(),
                Objects.requireNonNullElse(trip.imageUrl(), EMPTY_IMAGE_URL),
                Objects.requireNonNullElse(trip.routeImageUrl(), EMPTY_IMAGE_URL)
        );
    }
}
