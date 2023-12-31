package dev.tripdraw.post.presentation;

import static dev.tripdraw.post.presentation.PostControllerTest.PostRequestFixture.감상_생성_요청;
import static dev.tripdraw.post.presentation.PostControllerTest.PostRequestFixture.멀티파트_요청;
import static dev.tripdraw.post.presentation.PostControllerTest.PostRequestFixture.현재_위치_감상_생성_요청;
import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.PostFixture.새로운_감상;
import static dev.tripdraw.test.fixture.TripFixture.새로운_여행;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import dev.tripdraw.auth.application.JwtTokenProvider;
import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.post.dto.PostAndPointCreateRequest;
import dev.tripdraw.post.dto.PostCreateResponse;
import dev.tripdraw.post.dto.PostRequest;
import dev.tripdraw.post.dto.PostResponse;
import dev.tripdraw.post.dto.PostSearchResponse;
import dev.tripdraw.post.dto.PostUpdateRequest;
import dev.tripdraw.post.dto.PostsResponse;
import dev.tripdraw.post.dto.PostsSearchResponse;
import dev.tripdraw.test.ControllerTest;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class PostControllerTest extends ControllerTest {

    private static final String INVALID_TOKEN = "wrong.long.token";
    private static final String VERSION_HEADER = "X-version";
    private static final String V2 = "V2";

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
    void 현재_위치에_대한_감상을_생성한다() {
        // given
        PostAndPointCreateRequest request = 현재_위치_감상_생성_요청(trip.id());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .multiPart("dto", request, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .extract();

        // then
        PostCreateResponse postCreateResponse = response.as(PostCreateResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(CREATED.value());
            softly.assertThat(postCreateResponse.postId()).isNotNull();
        });
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_인증에_실패하면_예외를_발생시킨다() {
        // given
        PostAndPointCreateRequest request = 현재_위치_감상_생성_요청(trip.id());

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(INVALID_TOKEN)
                .multiPart("dto", request, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_존재하지_않는_여행의_ID이면_예외를_발생시킨다() {
        // given
        PostAndPointCreateRequest request = 현재_위치_감상_생성_요청(Long.MAX_VALUE);

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .multiPart("dto", request, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_제목이_비어있으면_예외를_발생시킨다() {
        // given
        PostAndPointCreateRequest request = 현재_위치_감상_생성_요청(trip.id(), "");

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .multiPart("dto", request, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_제목이_100자를_초과하면_예외를_발생시킨다() {
        // given
        String invalidTitle = "A".repeat(101);
        PostAndPointCreateRequest request = 현재_위치_감상_생성_요청(trip.id(), invalidTitle);

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .multiPart("dto", request, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성한다() {
        // given
        PostRequest request = 감상_생성_요청(trip.id(), point.id());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .multiPart("dto", request, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .extract();

        // then
        PostCreateResponse postCreateResponse = response.as(PostCreateResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(CREATED.value());
            softly.assertThat(postCreateResponse.postId()).isNotNull();
        });
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_인증에_실패하면_예외를_발생시킨다() {
        // given
        PostRequest request = 감상_생성_요청(trip.id(), point.id());

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(INVALID_TOKEN)
                .multiPart("dto", request, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_존재하지_않는_여행의_ID이면_예외를_발생시킨다() {
        // given
        Long invalidTripId = Long.MAX_VALUE;
        PostRequest request = 감상_생성_요청(invalidTripId, point.id());

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .multiPart("dto", request, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_존재하지_않는_위치의_ID이면_예외를_발생시킨다() {
        // given
        Long invalidPointId = Long.MAX_VALUE;
        PostRequest request = 감상_생성_요청(trip.id(), invalidPointId);

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .multiPart("dto", request, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_제목이_100자를_초과하면_예외를_발생시킨다() {
        // given
        String invalidTitle = "A".repeat(101);
        PostRequest request = 감상_생성_요청(trip.id(), point.id(), invalidTitle);

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .multiPart("dto", request, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
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
                .header(VERSION_HEADER, V2)
                .when().get("/posts/{postId}", post.id())
                .then().log().all()
                .extract();

        // then
        PostResponse postResponse = response.as(PostResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(postResponse)
                    .usingRecursiveComparison()
                    .ignoringFieldsOfTypes(LocalDateTime.class)
                    .isEqualTo(PostResponse.from(post, loginUser.memberId()));
        });
    }

    private void updateHasPost(final List<Point> points) {
        pointRepository.saveAll(points);
    }

    @Test
    void 특정_감상을_조회할_때_존재하지_않는_감상_ID이면_예외가_발생한다() {
        // given
        Long invalidPostId = Long.MIN_VALUE;

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .header(VERSION_HEADER, V2)
                .when().get("/posts/{postId}", invalidPostId)
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 위치정보_ID로_특정_감상을_조회한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id()));
        updateHasPost(List.of(point));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(accessToken)
                .header(VERSION_HEADER, V2)
                .when().get("/points/{pointId}/post", point.id())
                .then().log().all()
                .extract();

        // then
        PostResponse postResponse = response.as(PostResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(postResponse)
                    .usingRecursiveComparison()
                    .ignoringFieldsOfTypes(LocalDateTime.class)
                    .isEqualTo(PostResponse.from(post, loginUser.memberId()));
        });
    }

    @Test
    void 위치정보_ID로_특정_감상을_조회할_때_존재하지_않는_감상_ID이면_예외가_발생한다() {
        // given
        Long invalidPointId = Long.MIN_VALUE;

        // expect
        RestAssured.given().log().all()
                .auth().preemptive().oauth2(accessToken)
                .header(VERSION_HEADER, V2)
                .when().get("/points/{pointId}/post", invalidPointId)
                .then().log().all()
                .statusCode(NOT_FOUND.value());
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
                .header(VERSION_HEADER, V2)
                .when().get("/trips/{tripId}/posts", trip.id())
                .then().log().all()
                .extract();

        // then
        PostsResponse postsResponse = response.as(PostsResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(postsResponse.posts())
                    .usingRecursiveComparison()
                    .ignoringFieldsOfTypes(LocalDateTime.class)
                    .isEqualTo(List.of(
                            PostResponse.from(post2, loginUser.memberId()),
                            PostResponse.from(post1, loginUser.memberId())
                    ));
        });
    }

    @Test
    void 특정_여행에_대한_모든_감상을_조회할_때_존재하지_않는_여행의_ID이면_예외가_발생한다() {
        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .header(VERSION_HEADER, V2)
                .when().get("/trips/{tripId}/posts", Long.MIN_VALUE)
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 감상을_수정한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id()));
        PostUpdateRequest request = new PostUpdateRequest("우도의 땅콩 아이스크림", "수정한 내용입니다.");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .multiPart(멀티파트_요청(request))
                .when().patch("/posts/{postId}", post.id())
                .then().log().all()
                .extract();

        // then
        Post updatedPost = postRepository.getPostWithPointAndMemberById(post.id());
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
            softly.assertThat(updatedPost.title()).isEqualTo("우도의 땅콩 아이스크림");
            softly.assertThat(updatedPost.writing()).isEqualTo("수정한 내용입니다.");
        });
    }

    @Test
    void 감상을_수정할_때_인증에_실패하면_예외가_발생한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id()));
        PostUpdateRequest request = new PostUpdateRequest("우도의 땅콩 아이스크림", "수정한 내용입니다.");

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(INVALID_TOKEN)
                .multiPart(멀티파트_요청(request))
                .when().patch("/posts/{postId}", post.id())
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 감상을_수정할_때_존재하지_않는_여행의_ID이면_예외가_발생한다() {
        // given
        PostUpdateRequest request = new PostUpdateRequest("우도의 땅콩 아이스크림", "수정한 내용입니다.");

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .multiPart(멀티파트_요청(request))
                .when().patch("/posts/{postId}", Long.MIN_VALUE)
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 감상을_삭제한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id()));

        // when
        ExtractableResponse<Response> firstResponse = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .when().delete("/posts/{postId}", post.id())
                .then().log().all()
                .extract();
        ExtractableResponse<Response> secondResponse = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .when().get("/posts/{postId}", post.id())
                .then().log().all()
                .extract();

        // then
        assertSoftly(softly -> {
            softly.assertThat(firstResponse.statusCode()).isEqualTo(NO_CONTENT.value());
            softly.assertThat(secondResponse.statusCode()).isEqualTo(NOT_FOUND.value());
        });
    }

    @Test
    void 감상을_삭제할_때_인증에_실패하면_예외가_발생한다() {
        // given
        Post post = postRepository.save(새로운_감상(point, member.id()));

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(INVALID_TOKEN)
                .when().delete("/posts/{postId}", post.id())
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
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
                .header(VERSION_HEADER, V2)
                .params(params)
                .when().get("/posts")
                .then().log().all()
                .extract();

        // then
        PostsSearchResponse postsSearchResponse = response.as(PostsSearchResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(postsSearchResponse.posts())
                    .usingRecursiveComparison()
                    .ignoringFieldsOfTypes(LocalDateTime.class)
                    .isEqualTo(List.of(
                            PostSearchResponse.from(jejuAugustPost, member.id()),
                            PostSearchResponse.from(jejuJulyPost, member.id())
                    ));
        });
    }

    static class PostRequestFixture {
        public static PostAndPointCreateRequest 현재_위치_감상_생성_요청(Long tripId) {
            return new PostAndPointCreateRequest(
                    tripId,
                    "우도의 바닷가",
                    "제주특별자치도 제주시 애월읍",
                    "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                    1.1,
                    2.2,
                    LocalDateTime.of(2023, 7, 18, 20, 24)
            );
        }

        public static PostAndPointCreateRequest 현재_위치_감상_생성_요청(Long tripId, String title) {
            return new PostAndPointCreateRequest(
                    tripId,
                    title,
                    "제주특별자치도 제주시 애월읍",
                    "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                    1.1,
                    2.2,
                    LocalDateTime.of(2023, 7, 18, 20, 24)
            );
        }

        public static PostRequest 감상_생성_요청(Long tripId, Long pointId) {
            return new PostRequest(
                    tripId,
                    pointId,
                    "우도의 바닷가",
                    "제주특별자치도 제주시 애월읍",
                    "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
            );
        }

        public static PostRequest 감상_생성_요청(Long tripId, Long pointId, String title) {
            return new PostRequest(
                    tripId,
                    pointId,
                    title,
                    "제주특별자치도 제주시 애월읍",
                    "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
            );
        }

        public static MultiPartSpecification 멀티파트_요청(Object request) {
            return new MultiPartSpecBuilder(request)
                    .fileName("request")
                    .controlName("dto")
                    .mimeType("application/json")
                    .charset("UTF-8")
                    .build();
        }
    }
}
