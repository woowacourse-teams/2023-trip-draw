package dev.tripdraw.presentation.controller;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.post.PostPointCreateRequest;
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
import org.springframework.http.MediaType;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PostControllerTest extends ControllerTest {

    private static final String 통후추_BASE64 = "Bearer 7Ya17ZuE7LaU";
    private static final String 순후추_BASE64 = "Bearer 7Iic7ZuE7LaU";

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
        PostPointCreateRequest postPointCreateRequest = new PostPointCreateRequest(
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
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", 통후추_BASE64)
                .body(postPointCreateRequest)
                .when().post("/posts/current-location")
                .then().log().all()
                .extract();

        // then
        PostResponse postResponse = response.as(PostResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(CREATED.value());
            softly.assertThat(postResponse.postId()).isNotNull();
            softly.assertThat(postResponse.title()).isEqualTo("우도의 바닷가");
            softly.assertThat(postResponse.pointResponse().pointId()).isNotNull();
            softly.assertThat(postResponse.pointResponse().latitude()).isEqualTo(1.1);
        });
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_인증에_실패하면_예외를_발생시킨다() {
        // given
        PostPointCreateRequest postPointCreateRequest = new PostPointCreateRequest(
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
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", 순후추_BASE64)
                .body(postPointCreateRequest)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_존재하지_않는_여행의_ID이면_예외를_발생시킨다() {
        // given
        PostPointCreateRequest postPointCreateRequest = new PostPointCreateRequest(
                -1L,
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", 통후추_BASE64)
                .body(postPointCreateRequest)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_제목이_비어있으면_예외를_발생시킨다() {
        // given
        PostPointCreateRequest postPointCreateRequest = new PostPointCreateRequest(
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
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", 통후추_BASE64)
                .body(postPointCreateRequest)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_제목이_100자를_초과하면_예외를_발생시킨다() {
        // given
        PostPointCreateRequest postPointCreateRequest = new PostPointCreateRequest(
                trip.id(),
                "123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 a",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", 통후추_BASE64)
                .body(postPointCreateRequest)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_위도가_존재하지_않으면_예외를_발생시킨다() {
        // given
        PostPointCreateRequest postPointCreateRequest = new PostPointCreateRequest(
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
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", 통후추_BASE64)
                .body(postPointCreateRequest)
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
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", 통후추_BASE64)
                .body(postRequest)
                .when().post("/posts")
                .then().log().all()
                .extract();

        // then
        PostResponse postResponse = response.as(PostResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(CREATED.value());
            softly.assertThat(postResponse.postId()).isNotNull();
            softly.assertThat(postResponse.title()).isEqualTo("우도의 바닷가");
            softly.assertThat(postResponse.pointResponse().pointId()).isNotNull();
            softly.assertThat(postResponse.pointResponse().latitude()).isEqualTo(1.1);
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
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", 순후추_BASE64)
                .body(postRequest)
                .when().post("/posts")
                .then().log().all()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_존재하지_않는_여행의_ID이면_예외를_발생시킨다() {
        // given
        PointResponse pointResponse = createPoint();

        PostRequest postRequest = new PostRequest(
                -1L,
                pointResponse.pointId(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", 통후추_BASE64)
                .body(postRequest)
                .when().post("/posts")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_존재하지_않는_위치의_ID이면_예외를_발생시킨다() {
        // given
        PostRequest postRequest = new PostRequest(
                trip.id(),
                -1L,
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", 통후추_BASE64)
                .body(postRequest)
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
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", 통후추_BASE64)
                .body(postRequest)
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
                "123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 a",
                "제주특별자치도 제주시 애월읍 소길리",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", 통후추_BASE64)
                .body(postRequest)
                .when().post("/posts")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    private PointResponse createPoint() {
        PointCreateRequest request = new PointCreateRequest(
                trip.id(),
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", 통후추_BASE64)
                .body(request)
                .when().post("/points")
                .then().log().all()
                .extract();

        return response.as(PointResponse.class);
    }
}
