package dev.tripdraw.test;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.oauth.OauthType;
import dev.tripdraw.domain.post.Post;
import dev.tripdraw.domain.trip.Point;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripName;
import dev.tripdraw.domain.trip.TripStatus;
import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class TestFixture {

    public static Member 사용자() {
        return new Member(1L, "통후추", "", OauthType.KAKAO);
    }

    public static Point 위치정보() {
        return new Point(1L, 1.1, 2.2, false, LocalDateTime.now());
    }

    public static Trip 여행() {
        return new Trip(1L, TripName.from("통후추"), 사용자(), TripStatus.ONGOING, "", "");
    }

    public static Post 감상() {
        return new Post("감상 제목", 위치정보(), "주소", "감상", 사용자(), 1L);
    }
}
