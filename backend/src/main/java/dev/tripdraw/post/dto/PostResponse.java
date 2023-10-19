package dev.tripdraw.post.dto;

import dev.tripdraw.post.domain.Post;
import dev.tripdraw.trip.dto.PointResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

public record PostResponse(
        @Schema(description = "감상의 Id", example = "1")
        Long postId,

        @Schema(description = "감상이 속하는 여행의 Id", example = "1")
        Long tripId,

        @Schema(description = "나의 감상 확인 flag", example = "true")
        boolean isMine,

        @Schema(description = "작성자 닉네임", example = "통후추")
        String authorNickname,

        @Schema(description = "제목", example = "우도의 바닷가")
        String title,

        @Schema(description = "현재 위치의 주소", example = "제주도 제주시 우도면")
        String address,

        @Schema(description = "내용", example = "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.")
        String writing,

        @Schema(description = "감상이 저장된 위치 정보")
        PointResponse pointResponse,

        @Schema(description = "감상에 첨부된 사진의 URL", example = "dev.tripdraw.site/post-image/WD2FEF-EF2.jpg")
        String postImageUrl,

        @Schema(description = "감상이 있는 여행의 경로 이미지 URL", example = "dev.tripdraw.site/route-image/WD2FEF-EF2.jpg")
        String routeImageUrl
) {

    private static final String EMPTY_IMAGE_URL = "";

    public static PostResponse from(Post post, Long loginUserId) {
        return new PostResponse(
                post.id(),
                post.tripId(),
                post.member().id().equals(loginUserId),
                post.member().nickname(),
                post.title(),
                post.address(),
                post.writing(),
                PointResponse.from(post.point()),
                Objects.requireNonNullElse(post.postImageUrl(), EMPTY_IMAGE_URL),
                Objects.requireNonNullElse(post.routeImageUrl(), EMPTY_IMAGE_URL)
        );
    }
}
