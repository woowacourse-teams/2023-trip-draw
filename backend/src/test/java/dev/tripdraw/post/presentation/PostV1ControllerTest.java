package dev.tripdraw.post.presentation;

import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.PostFixture.새로운_감상;
import static dev.tripdraw.test.fixture.TripFixture.새로운_여행;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.tripdraw.auth.application.JwtTokenProvider;
import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.post.dto.PostResponse;
import dev.tripdraw.post.dto.PostSearchResponse;
import dev.tripdraw.post.dto.v1.PostResponseV1;
import dev.tripdraw.post.dto.v1.PostSearchResponseV1;
import dev.tripdraw.post.dto.v1.PostsResponseV1;
import dev.tripdraw.post.dto.v1.PostsSearchResponseV1;
import dev.tripdraw.test.ControllerTest;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class PostV1ControllerTest extends ControllerTest {

    private static final String INVALID_TOKEN = "wrong.long.token";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 이벤트를 통해 비동기로 경로 이미지를 생성하기 때문에, MockBean으로 둡니다.
    @MockBean
    private RouteImageGenerator routeImageGenerator;

    private Member member;
    private Trip trip;
    private Point point;
    private String accessToken;
    private LoginUser loginUser;

    @BeforeEach
    public void setUp() {
        super.setUp();
        member = memberRepository.save(사용자());
        trip = tripRepository.save(새로운_여행(member));
        point = pointRepository.save(새로운_위치정보(trip));
        accessToken = jwtTokenProvider.generateAccessToken(member.id().toString());
        loginUser = new LoginUser(member.id());
    }

    @Test
    void 특정_감상을_조회한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id()));
        updateHasPost(List.of(point));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .when().get("/posts/{postId}", post.id())
                .then().log().all()
                .extract();

        // then
        PostResponseV1 postResponseV1 = response.as(PostResponseV1.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(postResponseV1)
                    .usingRecursiveComparison()
                    .ignoringFieldsOfTypes(LocalDateTime.class)
                    .isEqualTo(PostResponseV1.from(PostResponse.from(post, loginUser.memberId())));
        });
    }

    private void updateHasPost(final List<Point> points) {
        pointRepository.saveAll(points);
    }

    @Test
    void 위치정보_ID로_특정_감상을_조회한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id()));
        updateHasPost(List.of(point));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(accessToken)
                .when().get("/points/{pointId}/post", point.id())
                .then().log().all()
                .extract();

        // then
        PostResponseV1 postResponseV1 = response.as(PostResponseV1.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(postResponseV1)
                    .usingRecursiveComparison()
                    .ignoringFieldsOfTypes(LocalDateTime.class)
                    .isEqualTo(PostResponseV1.from(PostResponse.from(post, loginUser.memberId())));
        });
    }

    @Test
    void 특정_여행에_대한_모든_감상을_조회한다() {
        // given
        Point point1 = pointRepository.save(새로운_위치정보(2023, 10, 23, 10, 23, trip));
        Point point2 = pointRepository.save(새로운_위치정보(2023, 10, 23, 10, 24, trip));
        Post post1 = postRepository.save(새로운_감상(point1, member.id()));
        Post post2 = postRepository.save(새로운_감상(point2, member.id()));
        updateHasPost(List.of(point1, point2));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .when().get("/trips/{tripId}/posts", trip.id())
                .then().log().all()
                .extract();

        // then
        PostsResponseV1 postsResponseV1 = response.as(PostsResponseV1.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(postsResponseV1.posts())
                    .usingRecursiveComparison()
                    .ignoringFieldsOfTypes(LocalDateTime.class)
                    .isEqualTo(List.of(
                            PostResponseV1.from(PostResponse.from(post2, loginUser.memberId())),
                            PostResponseV1.from(PostResponse.from(post1, loginUser.memberId()))
                    ));
        });
    }

    @Test
    void 다른_사용자들의_감상을_조회한다() {
        // given
        Point julyPoint = pointRepository.save(새로운_위치정보(LocalDateTime.of(2023, 7, 18, 20, 24), trip));
        Point augustPoint = pointRepository.save(새로운_위치정보(LocalDateTime.of(2023, 8, 18, 17, 24), trip));
        Point septemberPoint = pointRepository.save(새로운_위치정보(LocalDateTime.of(2023, 9, 18, 17, 24), trip));
        Post jejuJulyPost = postRepository.save(새로운_감상(julyPoint, member.id(), "제주특별자치도 제주시 애월읍", trip.id()));
        Post jejuAugustPost = postRepository.save(새로운_감상(augustPoint, member.id(), "제주특별자치도 제주시 애월읍", trip.id()));
        Post seoulSeptemberPost = postRepository.save(새로운_감상(septemberPoint, member.id(), "서울특별시 송파구 잠실동", trip.id()));
        updateHasPost(List.of(julyPoint, augustPoint, septemberPoint));
        Map<String, Object> params = Map.of("address", "제주특별자치도 제주시 애월읍", "limit", 10);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(accessToken)
                .params(params)
                .when().get("/posts")
                .then().log().all()
                .extract();

        // then
        PostsSearchResponseV1 postsSearchResponseV1 = response.as(PostsSearchResponseV1.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(postsSearchResponseV1.posts())
                    .usingRecursiveComparison()
                    .ignoringFieldsOfTypes(LocalDateTime.class)
                    .isEqualTo(List.of(
                            PostSearchResponseV1.from(PostSearchResponse.from(jejuAugustPost, member.id())),
                            PostSearchResponseV1.from(PostSearchResponse.from(jejuJulyPost, member.id()))
                    ));
        });
    }
}
