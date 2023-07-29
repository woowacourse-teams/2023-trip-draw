package dev.tripdraw.presentation.controller;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.trip.PointCreateRequest;
import dev.tripdraw.dto.trip.PointCreateResponse;
import dev.tripdraw.dto.trip.PointDeleteRequest;
import dev.tripdraw.dto.trip.PointResponse;
import dev.tripdraw.dto.trip.TripCreateResponse;
import dev.tripdraw.dto.trip.TripResponse;
import dev.tripdraw.dto.trip.TripSearchResponse;
import dev.tripdraw.dto.trip.TripsSearchResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TripControllerTest extends ControllerTest {

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
    void 여행을_생성한다() {
        // given & when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(통후추_BASE64)
                .when().post("/trips")
                .then().log().all()
                .extract();

        // then
        TripCreateResponse tripCreateResponse = response.as(TripCreateResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(CREATED.value());
            softly.assertThat(tripCreateResponse.tripId()).isNotNull();
        });
    }

    @Test
    void 여행_생성_시_인증에_실패하면_예외를_발생시킨다() {
        // given & expect
        RestAssured.given().log().all()
                .auth().preemptive().oauth2(순후추_BASE64)
                .when().post("/trips")
                .then().log().all()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void 여행에_위치_정보를_추가한다() {
        // given
        PointCreateRequest request = new PointCreateRequest(
                trip.id(),
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(통후추_BASE64)
                .body(request)
                .when().post("/points")
                .then().log().all()
                .extract();

        // then
        PointCreateResponse pointCreateResponse = response.as(PointCreateResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(CREATED.value());
            softly.assertThat(pointCreateResponse.pointId()).isNotNull();
        });
    }

    @Test
    void 위치_정보_추가_시_인증에_실패하면_예외를_발생시킨다() {
        // given
        PointCreateRequest request = new PointCreateRequest(
                trip.id(),
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(순후추_BASE64)
                .body(request)
                .when().post("/points")
                .then().log().all()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void 여행을_ID로_조회한다() {
        // given & when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(통후추_BASE64)
                .when().get("/trips/{tripId}", trip.id())
                .then().log().all()
                .extract();

        // then
        TripResponse tripResponse = response.as(TripResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(tripResponse.tripId()).isNotNull();
            softly.assertThat(tripResponse.name()).isNotNull();
            softly.assertThat(tripResponse.routes()).isNotNull();
            softly.assertThat(tripResponse.status()).isNotNull();
        });
    }

    @Test
    void 특정_위치정보를_삭제한다() {
        // given
        PointCreateRequest pointCreateRequest = new PointCreateRequest(
                trip.id(),
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(통후추_BASE64)
                .body(pointCreateRequest)
                .when().post("/points")
                .then().log().all()
                .extract();

        PointResponse pointResponse = response.as(PointResponse.class);

        PointDeleteRequest pointDeleteRequest = new PointDeleteRequest(trip.id(), pointResponse.pointId());

        // expect
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(통후추_BASE64)
                .body(pointDeleteRequest)
                .when().delete("/points")
                .then().log().all()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    void 특정_위치정보_삭제시_인증에_실패하면_예외를_발생시킨다() {
        // given
        PointCreateRequest pointCreateRequest = new PointCreateRequest(
                trip.id(),
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(통후추_BASE64)
                .body(pointCreateRequest)
                .when().post("/points")
                .then().log().all()
                .extract();

        PointResponse pointResponse = response.as(PointResponse.class);

        PointDeleteRequest pointDeleteRequest = new PointDeleteRequest(trip.id(), pointResponse.pointId());

        // expect
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(순후추_BASE64)
                .body(pointDeleteRequest)
                .when().delete("/points")
                .then().log().all()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void 특정_위치정보_삭제시_해당_여행에_존재하는_위치정보가_아니면_예외를_발생시킨다() {
        // given

        PointCreateRequest pointCreateRequest = new PointCreateRequest(
                trip.id(),
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        PointResponse pointResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(통후추_BASE64)
                .body(pointCreateRequest)
                .when().post("/points")
                .then().log().all()
                .extract()
                .as(PointResponse.class);

        TripResponse tripResponse = RestAssured.given().log().all()
                .auth().preemptive().oauth2(통후추_BASE64)
                .when().post("/trips")
                .then().log().all()
                .extract()
                .as(TripResponse.class);

        PointDeleteRequest pointDeleteRequest = new PointDeleteRequest(tripResponse.tripId(), pointResponse.pointId());

        // expect
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(통후추_BASE64)
                .body(pointDeleteRequest)
                .when().delete("/points")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 삭제된_위치정보를_삭제시_예외를_발생시킨다() {
        // given
        PointCreateRequest pointCreateRequest = new PointCreateRequest(
                trip.id(),
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(통후추_BASE64)
                .body(pointCreateRequest)
                .when().post("/points")
                .then().log().all()
                .extract();

        PointResponse pointResponse = response.as(PointResponse.class);
        PointDeleteRequest pointDeleteRequest = new PointDeleteRequest(trip.id(), pointResponse.pointId());

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(통후추_BASE64)
                .body(pointDeleteRequest)
                .when().delete("/points")
                .then().log().all();

        // expect
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(통후추_BASE64)
                .body(pointDeleteRequest)
                .when().delete("/points")
                .then().log().all()
                .statusCode(CONFLICT.value());
    }

    @Test
    void 전체_여행을_조회한다() {
        // given
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(통후추_BASE64)
                .when().get("/trips")
                .then().log().all()
                .extract();

        // then
        TripsSearchResponse tripsSearchResponse = response.as(TripsSearchResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(tripsSearchResponse).usingRecursiveComparison().isEqualTo(
                    new TripsSearchResponse(List.of(new TripSearchResponse(trip.id(), trip.nameValue())))
            );
        });
    }
}
