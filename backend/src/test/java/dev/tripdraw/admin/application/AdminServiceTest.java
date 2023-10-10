package dev.tripdraw.admin.application;

import static dev.tripdraw.test.fixture.MemberFixture.새로운_사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.PostFixture.새로운_감상;
import static dev.tripdraw.test.fixture.TripFixture.새로운_여행;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.admin.dto.AdminPostResponse;
import dev.tripdraw.admin.dto.AdminPostsResponse;
import dev.tripdraw.admin.dto.AdminTripResponse;
import dev.tripdraw.admin.dto.AdminTripsResponse;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(새로운_사용자("통후추"));
    }

    @Test
    void 여행을_전체_조회한다() {
        // given
        for (int i = 0; i < 11; i++) {
            tripRepository.save(새로운_여행(member));
        }

        // when
        AdminTripsResponse trips = adminService.readTrips(null, 10);

        // then
        assertSoftly(softly -> {
            softly.assertThat(trips.items()).hasSize(10);
            softly.assertThat(trips.hasNextPage()).isTrue();
        });
    }

    @Test
    void 여행을_조회한다() {
        // given
        Trip trip = tripRepository.save(새로운_여행(member));

        // when
        AdminTripResponse response = adminService.readTripById(trip.id());

        // then
        assertThat(response).isEqualTo(AdminTripResponse.from(trip));
    }

    @Test
    void 여행을_삭제한다() {
        // given
        Trip trip = tripRepository.save(새로운_여행(member));

        // when
        adminService.deleteTrip(trip.id());

        // then
        assertThat(tripRepository.existsById(trip.id())).isFalse();
    }

    @Test
    void 감상을_전체_조회한다() {
        // given
        Trip trip = tripRepository.save(새로운_여행(member));
        for (int i = 0; i < 11; i++) {
            Point point = pointRepository.save(새로운_위치정보(trip));
            postRepository.save(새로운_감상(point, member.id(), String.valueOf(i + 1)));
            pointRepository.save(point);
        }

        // when
        AdminPostsResponse posts = adminService.readPosts(null, 10);

        // then
        assertSoftly(softly -> {
            softly.assertThat(posts.items()).hasSize(10);
            softly.assertThat(posts.hasNextPage()).isTrue();
        });
    }

    @Test
    void 감상을_조회한다() {
        // given
        Trip trip = tripRepository.save(새로운_여행(member));
        Point point = pointRepository.save(새로운_위치정보(trip));
        Post post = postRepository.save(새로운_감상(point, member.id()));
        pointRepository.save(point);

        // when
        AdminPostResponse response = adminService.readPostById(post.id());

        // then
        assertThat(response).isEqualTo(AdminPostResponse.from(post));
    }

    @Test
    void 감상을_삭제한다() {
        // given
        Trip trip = tripRepository.save(새로운_여행(member));
        Point point = pointRepository.save(새로운_위치정보(trip));
        Post post = postRepository.save(새로운_감상(point, member.id()));

        // when
        adminService.deletePost(post.id());

        // then
        assertThat(postRepository.existsById(post.id())).isFalse();
    }
}
