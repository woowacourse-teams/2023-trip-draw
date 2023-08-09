package dev.tripdraw.presentation.controller;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import dev.tripdraw.application.draw.RouteImageGenerator;
import dev.tripdraw.application.oauth.AuthTokenManager;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.post.PostAndPointCreateRequest;
import dev.tripdraw.dto.post.PostCreateResponse;
import dev.tripdraw.dto.post.PostRequest;
import dev.tripdraw.dto.post.PostResponse;
import dev.tripdraw.dto.post.PostUpdateRequest;
import dev.tripdraw.dto.post.PostsResponse;
import dev.tripdraw.dto.trip.PointCreateRequest;
import dev.tripdraw.dto.trip.PointResponse;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PostControllerTest extends ControllerTest {

    private static final String WRONG_TOKEN = "wrong.long.token";

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthTokenManager authTokenManager;

    @MockBean
    private RouteImageGenerator routeImageGenerator;

    private Trip trip;
    private String huchuToken;

    @BeforeEach
    void setUp() {
        super.setUp();

        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        trip = tripRepository.save(Trip.from(member));
        huchuToken = authTokenManager.generate(member.id());
    }

    @Test
    void 현재_위치에_대한_감상을_생성한다() {
        // given
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postAndPointCreateRequest, APPLICATION_JSON_VALUE)
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
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .multiPart("dto", postAndPointCreateRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_존재하지_않는_여행의_ID이면_예외를_발생시킨다() {
        // given
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                Long.MIN_VALUE,
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postAndPointCreateRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_제목이_비어있으면_예외를_발생시킨다() {
        // given
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postAndPointCreateRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_제목이_100자를_초과하면_예외를_발생시킨다() {
        // given
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "a".repeat(101),
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postAndPointCreateRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_위도가_존재하지_않으면_예외를_발생시킨다() {
        // given
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                null,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postAndPointCreateRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성한다() {
        // given
        PointResponse pointResponse = createPoint();

        PostRequest postRequest = new PostRequest(
                trip.id(),
                pointResponse.pointId(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postRequest, APPLICATION_JSON_VALUE)
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
        PointResponse pointResponse = createPoint();

        PostRequest postRequest = new PostRequest(
                trip.id(),
                pointResponse.pointId(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .multiPart("dto", postRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_존재하지_않는_여행의_ID이면_예외를_발생시킨다() {
        // given
        PointResponse pointResponse = createPoint();

        PostRequest postRequest = new PostRequest(
                Long.MIN_VALUE,
                pointResponse.pointId(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_존재하지_않는_위치의_ID이면_예외를_발생시킨다() {
        // given
        PostRequest postRequest = new PostRequest(
                trip.id(),
                Long.MIN_VALUE,
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_주소가_비어있으면_예외를_발생시킨다() {
        // given
        PointResponse pointResponse = createPoint();

        PostRequest postRequest = new PostRequest(
                trip.id(),
                pointResponse.pointId(),
                "우도의 바닷가",
                null,
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_제목이_100자를_초과하면_예외를_발생시킨다() {
        // given
        PointResponse pointResponse = createPoint();

        PostRequest postRequest = new PostRequest(
                trip.id(),
                pointResponse.pointId(),
                "a".repeat(101),
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 특정_감상을_조회한다() {
        // given
        PostCreateResponse postResponse = createPost();

        // when
        ExtractableResponse<Response> findResponse = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/posts/{postId}", postResponse.postId())
                .then().log().all()
                .extract();

        PostResponse getResponse = findResponse.as(PostResponse.class);

        // then
        assertSoftly(softly -> {
            softly.assertThat(findResponse.statusCode()).isEqualTo(OK.value());
            softly.assertThat(getResponse.postId()).isNotNull();
            softly.assertThat(getResponse.title()).isEqualTo("우도의 바닷가");
            softly.assertThat(getResponse.pointResponse().pointId()).isNotNull();
            softly.assertThat(getResponse.pointResponse().latitude()).isEqualTo(1.1);
            softly.assertThat(getResponse.postImageUrl()).isNull();
        });
    }

    @Test
    void 특정_감상을_조회할_때_인증에_실패하면_예외가_발생한다() {
        // given
        PostCreateResponse postCreateResponse = createPost();

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .when().get("/posts/{postId}", postCreateResponse.postId())
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 특정_감상을_조회할_때_존재하지_않는_감상_ID이면_예외가_발생한다() {
        // given & expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/posts/{postId}", -1)
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 특정_여행에_대한_모든_감상을_조회한다() {
        // given
        createPost();
        createPost();

        // when
        ExtractableResponse<Response> findResponse = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/trips/{tripId}/posts", trip.id())
                .then().log().all()
                .extract();

        PostsResponse postsResponse = findResponse.as(PostsResponse.class);

        // then
        assertSoftly(softly -> {
            softly.assertThat(findResponse.statusCode()).isEqualTo(OK.value());
            softly.assertThat(postsResponse.posts().get(0).postId()).isNotNull();
            softly.assertThat(postsResponse.posts().get(0).title()).isEqualTo("우도의 바닷가");
            softly.assertThat(postsResponse.posts().get(0).pointResponse().pointId()).isNotNull();
            softly.assertThat(postsResponse.posts().get(0).pointResponse().latitude()).isEqualTo(1.1);
            softly.assertThat(postsResponse.posts().get(1).postId()).isNotNull();
            softly.assertThat(postsResponse.posts().get(1).title()).isEqualTo("우도의 바닷가");
            softly.assertThat(postsResponse.posts().get(1).pointResponse().pointId()).isNotNull();
            softly.assertThat(postsResponse.posts().get(1).pointResponse().latitude()).isEqualTo(1.1);
        });
    }

    @Test
    void 특정_여행에_대한_모든_감상을_조회할_때_인증에_실패하면_예외가_발생한다() {
        // given & expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .when().get("/trips/{tripId}/posts", trip.id())
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 특정_여행에_대한_모든_감상을_조회할_때_존재하지_않는_여행의_ID이면_예외가_발생한다() {
        // given & expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .when().get("/trips/{tripId}/posts", Long.MIN_VALUE)
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 감상을_수정한다() {
        // given
        PostCreateResponse postCreateResponse = createPost();

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                "우도의 땅콩 아이스크림",
                "수정한 내용입니다."
        );

        MultiPartSpecification multiPartSpecification = getMultiPartSpecification(postUpdateRequest);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart(multiPartSpecification)
                .when().patch("/posts/{postId}", postCreateResponse.postId())
                .then().log().all()
                .extract();

        // then
        PostResponse postResponse = readPost(postCreateResponse.postId());

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
            softly.assertThat(postResponse.title()).isEqualTo("우도의 땅콩 아이스크림");
            softly.assertThat(postResponse.writing()).isEqualTo("수정한 내용입니다.");
        });
    }

    @Test
    void 감상을_수정할_때_인증에_실패하면_예외가_발생한다() {
        // given
        PostCreateResponse postCreateResponse = createPost();

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                "우도의 땅콩 아이스크림",
                "수정한 내용입니다."
        );

        MultiPartSpecification multiPartSpecification = getMultiPartSpecification(postUpdateRequest);

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .multiPart(multiPartSpecification)
                .when().patch("/posts/{postId}", postCreateResponse.postId())
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 감상을_수정할_때_존재하지_않는_여행의_ID이면_예외가_발생한다() {
        // given
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                "우도의 땅콩 아이스크림",
                "수정한 내용입니다."
        );

        MultiPartSpecification multiPartSpecification = getMultiPartSpecification(postUpdateRequest);

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart(multiPartSpecification)
                .when().patch("/posts/{postId}", Long.MIN_VALUE)
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 감상을_삭제한다() {
        // given
        PostCreateResponse postCreateResponse = createPost();

        // expect1 : 삭제하면 204 NO_CONTENT 응답
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .when().delete("/posts/{postId}", postCreateResponse.postId())
                .then().log().all()
                .statusCode(NO_CONTENT.value());

        // expect2 : 다시 조회하면 404 NOT_FOUND 응답
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/posts/{postId}", postCreateResponse.postId())
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 감상을_삭제할_때_인증에_실패하면_예외가_발생한다() {
        // given
        PostCreateResponse postCreateResponse = createPost();

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .when().delete("/posts/{postId}", postCreateResponse.postId())
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 감상을_삭제할_때_존재하지_않는_여행의_ID이면_예외가_발생한다() {
        // given & expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .when().delete("/posts/{postId}", Long.MIN_VALUE)
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    private PointResponse createPoint() {
        PointCreateRequest request = new PointCreateRequest(
                trip.id(),
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .body(request)
                .when().post("/points")
                .then().log().all()
                .extract();

        return response.as(PointResponse.class);
    }

    private PostCreateResponse createPost() {
        // given
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        MultiPartSpecification multiPartSpecification = new MultiPartSpecBuilder(postAndPointCreateRequest)
                .fileName("postAndPointCreateRequest")
                .controlName("dto")
                .mimeType("application/json")
                .charset("UTF-8")
                .build();

        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart(multiPartSpecification)
                .when().post("/posts/current-location")
                .then().log().all()
                .extract();

        PostCreateResponse postResponse = createResponse.as(PostCreateResponse.class);
        return postResponse;
    }

    @Test
    PostResponse readPost(Long postId) {
        ExtractableResponse<Response> findResponse = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/posts/{postId}", postId)
                .then().log().all()
                .extract();

        return findResponse.as(PostResponse.class);
    }

    private MultiPartSpecification getMultiPartSpecification(Object request) {
        return new MultiPartSpecBuilder(request)
                .fileName("request")
                .controlName("dto")
                .mimeType("application/json")
                .charset("UTF-8")
                .build();
    }
}
