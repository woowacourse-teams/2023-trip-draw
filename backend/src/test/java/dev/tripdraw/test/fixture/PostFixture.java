package dev.tripdraw.test.fixture;

import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static dev.tripdraw.test.fixture.PointFixture.위치정보;

import dev.tripdraw.post.domain.Post;

public class PostFixture {

    public static Post 감상() {
        return new Post("감상 제목", 위치정보(), "주소", "감상", 사용자().id(), 1L);
    }
}
