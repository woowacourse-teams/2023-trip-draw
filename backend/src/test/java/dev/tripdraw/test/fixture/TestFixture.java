package dev.tripdraw.test.fixture;

import static dev.tripdraw.trip.domain.TripStatus.FINISHED;

import dev.tripdraw.common.auth.OauthType;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.dto.PostAndPointCreateRequest;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripName;
import dev.tripdraw.trip.domain.TripStatus;
import dev.tripdraw.trip.dto.PointCreateRequest;
import dev.tripdraw.trip.dto.TripUpdateRequest;
import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class TestFixture {

    public static Member 사용자() {
        return new Member(1L, "통후추", "", OauthType.KAKAO);
    }

    public static Point 위치정보() {
        return new Point(1L, 1.1, 2.2, false, LocalDateTime.now(), 여행());
    }

    public static Point 위치정보(int year, int month, int dayOfMonth, int hour, int minute) {
        return new Point(1.1, 2.2, LocalDateTime.of(year, month, dayOfMonth, hour, minute));
    }

    public static Trip 여행() {
        return new Trip(1L, TripName.from("통후추"), 사용자(), TripStatus.ONGOING, "", "");
    }

    public static Post 감상() {
        return new Post("감상 제목", 위치정보(), "주소", "감상", 사용자(), 1L);
    }

    public static PointCreateRequest pointCreateRequest(Long tripId) {
        return new PointCreateRequest(
                tripId,
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );
    }

    public static TripUpdateRequest tripUpdateRequest() {
        return new TripUpdateRequest("제주도 여행", FINISHED);
    }

    public static PostAndPointCreateRequest postAndPointCreateRequest(Long tripId) {
        return new PostAndPointCreateRequest(
                tripId,
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );
    }

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
