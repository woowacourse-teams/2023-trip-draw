package dev.tripdraw.test.fixture;

import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static dev.tripdraw.test.fixture.PointFixture.위치정보;

import dev.tripdraw.post.domain.Post;
import dev.tripdraw.trip.domain.Point;

public class PostFixture {

    public static Post 감상() {
        return new Post(1L, "감상 제목", 위치정보(), "주소", "감상", 사용자(), 1L);
    }

    public static Post 새로운_감상(Point point, Long memberId) {
        return new Post("감상 제목", point, "주소", "감상", 사용자(memberId), 1L);
    }

    public static Post 새로운_감상(Point point, Long memberId, String address) {
        return new Post("감상 제목", point, address, "감상", 사용자(memberId), 1L);
    }

    public static Post 새로운_감상(Point point, Long memberId, String address, Long tripId) {
        return new Post("감상 제목", point, address, "감상", 사용자(memberId), tripId);
    }
}
