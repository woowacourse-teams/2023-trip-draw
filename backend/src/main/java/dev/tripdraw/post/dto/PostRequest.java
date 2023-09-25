package dev.tripdraw.post.dto;

import dev.tripdraw.post.domain.Post;
import dev.tripdraw.trip.domain.Point;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @Schema(description = "감상이 속하는 여행의 Id", example = "1")
        @NotNull
        Long tripId,

        @Schema(description = "감상을 저장할 위치의 Id", example = "1")
        @NotNull
        Long pointId,

        @Schema(description = "제목", example = "우도의 바닷가")
        @NotBlank @Size(max = 100)
        String title,

        @Schema(description = "현재 위치의 주소", example = "제주도 제주시 우도면")
        @NotBlank
        String address,

        @Schema(description = "내용", example = "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.")
        @Size(max = 10_000)
        String writing
) {

    public Post toPost(Long memberId, Point point) {
        return new Post(title, point, address, writing, memberId, tripId);
    }
}
