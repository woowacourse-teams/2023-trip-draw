package dev.tripdraw.presentation.controller;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.post.PostAndPointCreateRequest;
import dev.tripdraw.dto.post.PostCreateResponse;
import dev.tripdraw.dto.post.PostRequest;
import dev.tripdraw.dto.post.PostResponse;
import dev.tripdraw.dto.trip.PointCreateRequest;
import dev.tripdraw.dto.trip.PointResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PostControllerTest extends ControllerTest {

    private static final String 통후추_BASE64 = "7Ya17ZuE7LaU";
    private static final String 순후추_BASE64 = "7Iic7ZuE7LaU";

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Trip trip;

    @BeforeEach
    void setUp() {
        super.setUp();

        Member member = memberRepository.save(new Member("통후추"));
        trip = tripRepository.save(Trip.from(member));
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
                .auth().preemptive().oauth2(통후추_BASE64)
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
                .auth().preemptive().oauth2(순후추_BASE64)
                .multiPart("dto", postAndPointCreateRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(FORBIDDEN.value());
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
                .auth().preemptive().oauth2(통후추_BASE64)
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
                .auth().preemptive().oauth2(통후추_BASE64)
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
                .auth().preemptive().oauth2(통후추_BASE64)
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
                .auth().preemptive().oauth2(통후추_BASE64)
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
                .auth().preemptive().oauth2(통후추_BASE64)
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
                .auth().preemptive().oauth2(순후추_BASE64)
                .multiPart("dto", postRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(FORBIDDEN.value());
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
                .auth().preemptive().oauth2(통후추_BASE64)
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
                .auth().preemptive().oauth2(통후추_BASE64)
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
                .auth().preemptive().oauth2(통후추_BASE64)
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
                .auth().preemptive().oauth2(통후추_BASE64)
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
                .auth().preemptive().oauth2(통후추_BASE64)
                .when().get("/posts/" + postResponse.postId())
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
        });
    }

    @Test
    void 특정_감상을_조회할_때_인증에_실패하면_예외가_발생한다() {
        // given
        PostCreateResponse postCreateResponse = createPost();

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(순후추_BASE64)
                .when().get("/posts/" + postCreateResponse.postId())
                .then().log().all()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void 특정_감상을_조회할_때_존재하지_않는_감상_ID이면_예외가_발생한다() {
        // given & expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(통후추_BASE64)
                .when().get("/posts/-1")
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
                .auth().preemptive().oauth2(통후추_BASE64)
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

        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(통후추_BASE64)
                .multiPart("dto", postAndPointCreateRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .extract();

        PostCreateResponse postResponse = createResponse.as(PostCreateResponse.class);
        return postResponse;
    }
}
