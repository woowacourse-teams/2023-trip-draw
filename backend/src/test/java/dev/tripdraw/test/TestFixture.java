package dev.tripdraw.test;

import dev.tripdraw.auth.domain.OauthType;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripName;
import dev.tripdraw.trip.domain.TripStatus;
import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class TestFixture {

    public static Member 사용자() {
        return new Member(1L, "통후추", "", OauthType.KAKAO);
    }

    public static Point 위치정보() {
        return new Point(1L, 1.1, 2.2, false, LocalDateTime.now(), 여행());
    }

    public static Trip 여행() {
        return new Trip(1L, TripName.from("통후추"), 사용자(), TripStatus.ONGOING, "", "");
    }

    public static Post 감상() {
        return new Post("감상 제목", 위치정보(), "주소", "감상", 사용자(), 1L);
    }
}
