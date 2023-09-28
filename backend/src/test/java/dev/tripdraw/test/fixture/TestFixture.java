package dev.tripdraw.test.fixture;

import dev.tripdraw.post.dto.PostAndPointCreateRequest;
import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class TestFixture {

    public static PostAndPointCreateRequest 제주_2023_2_1_수(Long tripId) {
        return new PostAndPointCreateRequest(
                tripId,
                "제목",
                "제주특별자치도 제주시 애월읍",
                "내용",
                0.0,
                0.0,
                LocalDateTime.of(2023, 2, 1, 1, 1)
        );
    }

    public static PostAndPointCreateRequest 서울_2023_1_1_일(Long tripId) {
        return new PostAndPointCreateRequest(
                tripId,
                "제목",
                "seoul_songpa_sincheon",
                "내용",
                0.0,
                0.0,
                LocalDateTime.of(2023, 1, 1, 10, 1)
        );
    }

    public static PostAndPointCreateRequest 제주_2023_1_1_일(Long tripId) {
        return new PostAndPointCreateRequest(
                tripId,
                "제목",
                "제주특별자치도 제주시 애월읍",
                "내용",
                0.0,
                0.0,
                LocalDateTime.of(2023, 1, 1, 1, 1)
        );
    }

    public static PostAndPointCreateRequest 서울_2022_1_2_일(Long tripId) {
        return new PostAndPointCreateRequest(
                tripId,
                "제목",
                "seoul_songpa_Bangi",
                "내용",
                0.0,
                0.0,
                LocalDateTime.of(2022, 1, 2, 1, 1)
        );
    }

    public static PostAndPointCreateRequest 양양_2021_3_2_화(Long tripId) {
        return new PostAndPointCreateRequest(
                tripId,
                "제목",
                "강원도 양양",
                "내용",
                0.0,
                0.0,
                LocalDateTime.of(2021, 3, 2, 10, 1)
        );
    }
}
