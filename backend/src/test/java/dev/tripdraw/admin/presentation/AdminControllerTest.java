package dev.tripdraw.admin.presentation;

import static dev.tripdraw.test.fixture.MemberFixture.새로운_사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.PostFixture.새로운_감상;
import static dev.tripdraw.test.fixture.TripFixture.새로운_여행;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import dev.tripdraw.admin.application.AdminAuthService;
import dev.tripdraw.admin.application.PasswordEncoder;
import dev.tripdraw.admin.domain.Admin;
import dev.tripdraw.admin.domain.AdminRepository;
import dev.tripdraw.admin.dto.AdminLoginRequest;
import dev.tripdraw.admin.dto.AdminPostResponse;
import dev.tripdraw.admin.dto.AdminPostsResponse;
import dev.tripdraw.admin.dto.AdminTripResponse;
import dev.tripdraw.admin.dto.AdminTripsResponse;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.test.ControllerTest;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminControllerTest extends ControllerTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminAuthService adminAuthService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Member member;
    private String sessionId;

    @BeforeEach
    public void setUp() {
        super.setUp();
        member = memberRepository.save(새로운_사용자("통후추"));
        Admin admin = adminRepository.save(new Admin("tripdraw@tripdraw.site", passwordEncoder.encode("password")));
        sessionId = adminAuthService.login(new AdminLoginRequest(admin.email(), "password"));
    }

    @Test
    void 여행을_전체_조회한다() {
        // given
        for (int i = 0; i < 11; i++) {
            tripRepository.save(새로운_여행(member));
        }

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("SESSION", sessionId)
                .params("limit", 10)
                .when().get("/admin/trips")
                .then().log().all()
                .extract();

        // then
        AdminTripsResponse adminTripsResponse = response.as(AdminTripsResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(adminTripsResponse.hasNextPage()).isTrue();
            softly.assertThat(adminTripsResponse.items()).hasSize(10);
        });
    }

    @Test
    void 여행을_조회한다() {
        // given
        Trip trip = tripRepository.save(새로운_여행(member));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("SESSION", sessionId)
                .when().get("/admin/trips/{tripId}", trip.id())
                .then().log().all()
                .extract();

        // then
        AdminTripResponse adminTripResponse = response.as(AdminTripResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(adminTripResponse).isEqualTo(AdminTripResponse.from(trip));
        });
    }

    @Test
    void 여행을_삭제한다() {
        // given
        Trip trip = tripRepository.save(새로운_여행(member));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("SESSION", sessionId)
                .when().delete("/admin/trips/{tripId}", trip.id())
                .then().log().all()
                .extract();

        // then
        Optional<Trip> findTrip = tripRepository.findById(trip.id());
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
            softly.assertThat(findTrip).isNotPresent();
        });
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
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("SESSION", sessionId)
                .params("limit", 10)
                .when().get("/admin/posts")
                .then().log().all()
                .extract();

        // then
        AdminPostsResponse adminPostsResponse = response.as(AdminPostsResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(adminPostsResponse.hasNextPage()).isTrue();
            softly.assertThat(adminPostsResponse.items()).hasSize(10);
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
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("SESSION", sessionId)
                .when().get("/admin/posts/{postId}", post.id())
                .then().log().all()
                .extract();

        // then
        AdminPostResponse adminPostResponse = response.as(AdminPostResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(adminPostResponse)
                    .usingRecursiveComparison()
                    .ignoringFieldsOfTypes(LocalDateTime.class)
                    .isEqualTo(AdminPostResponse.from(post));
        });
    }

    @Test
    void 감상을_삭제한다() {
        // given
        Trip trip = tripRepository.save(새로운_여행(member));
        Point point = pointRepository.save(새로운_위치정보(trip));
        Post post = postRepository.save(새로운_감상(point, member.id()));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .header("SESSION", sessionId)
                .when().delete("/admin/posts/{postId}", post.id())
                .then().log().all()
                .extract();

        // then
        Optional<Post> findPost = postRepository.findById(post.id());
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
            softly.assertThat(findPost).isNotPresent();
        });
    }
}
