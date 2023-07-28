package dev.tripdraw.dto.post;

import dev.tripdraw.domain.post.Post;
import dev.tripdraw.dto.trip.PointResponse;
import io.swagger.v3.oas.annotations.media.Schema;

public record PostResponse(
        @Schema(description = "감의 Id", example = "1")
        Long postId,

        @Schema(description = "감상이 속하는 여행의 Id", example = "1")
        Long tripId,

        @Schema(description = "제목", example = "우도의 바닷가")
        String title,

        @Schema(description = "현재 위치의 주소", example = "제주도 제주시 우도면")
        String address,

        @Schema(description = "내용", example = "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.")
        String writing,

        @Schema(description = "감상이 저장된 위치 정보")
        PointResponse pointResponse
) {

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.id(),
                post.tripId(),
                post.title(),
                post.address(),
                post.writing(),
                PointResponse.from(post.point())
        );
    }
}
