package dev.tripdraw.dto.post;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.post.Post;
import dev.tripdraw.trip.domain.Point;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record PostAndPointCreateRequest(
        @Schema(description = "감상이 속하는 여행의 Id", example = "1")
        @NotNull
        Long tripId,

        @Schema(description = "제목", example = "우도의 바닷가")
        @NotBlank
        @Size(max = 100)
        String title,

        @Schema(description = "현재 위치의 주소", example = "제주도 제주시 우도면")
        @NotBlank
        String address,

        @Schema(description = "내용", example = "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.")
        @Size(max = 10_000)
        String writing,

        @Schema(description = "현재 위치의 위도", example = "37.56663888630603")
        @NotNull
        Double latitude,

        @Schema(description = "현재 위치의 경도", example = "126.97838310403904")
        @NotNull
        Double longitude,

        @NotNull
        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        @Schema(description = "yyyy-MM-dd'T'HH:mm:ss 형식", example = "2023-07-23T19:48:27", type = "string")
        LocalDateTime recordedAt
) {

    public Point toPoint() {
        return new Point(latitude, longitude, recordedAt);
    }

    public Post toPost(Member member, Point point) {
        return new Post(title, point, address, writing, member, tripId);
    }
}
