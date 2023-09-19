package dev.tripdraw.trip.presentation;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.test.fixture.TestFixture.pointCreateRequest;
import static dev.tripdraw.test.fixture.TestFixture.tripUpdateRequest;
import static dev.tripdraw.test.step.TripStep.addPointAndGetResponse;
import static dev.tripdraw.test.step.TripStep.createTripAndGetResponse;
import static dev.tripdraw.test.step.TripStep.searchTripAndGetResponse;
import static dev.tripdraw.test.step.TripStep.updateTrip;
import static dev.tripdraw.trip.domain.TripStatus.FINISHED;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.tripdraw.auth.application.JwtTokenProvider;
import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.test.ControllerTest;
import dev.tripdraw.trip.dto.PointCreateRequest;
import dev.tripdraw.trip.dto.PointCreateResponse;
import dev.tripdraw.trip.dto.PointResponse;
import dev.tripdraw.trip.dto.TripCreateResponse;
import dev.tripdraw.trip.dto.TripResponse;
import dev.tripdraw.trip.dto.TripSearchResponseOfMember;
import dev.tripdraw.trip.dto.TripUpdateRequest;
import dev.tripdraw.trip.dto.TripsSearchResponseOfMember;
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
import org.springframework.boot.test.mock.mockito.MockBean;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TripControllerTest extends ControllerTest {

    private static final String WRONG_TOKEN = "wrong.long.token";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private RouteImageGenerator routeImageGenerator;

    private String huchuToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        Member huchu = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        huchuToken = jwtTokenProvider.generateAccessToken(huchu.id().toString());
    }

    @Test
    void 여행을_생성한다() {
        // given & when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
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
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .when().post("/trips")
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 여행에_위치_정보를_추가한다() {
        // given
        TripCreateResponse tripResponse = createTripAndGetResponse(huchuToken);
        PointCreateRequest request = new PointCreateRequest(
                tripResponse.tripId(),
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
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
        TripCreateResponse tripResponse = createTripAndGetResponse(huchuToken);
        PointCreateRequest request = new PointCreateRequest(
                tripResponse.tripId(),
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .body(request)
                .when().post("/points")
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 여행을_ID로_조회한다() {
        // given
        Long tripId = createTripAndGetResponse(huchuToken).tripId();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/trips/{tripId}", tripId)
                .then().log().all()
                .extract();

        // then
        TripResponse tripResponse = response.as(TripResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(tripResponse.tripId()).isNotNull();
            softly.assertThat(tripResponse.name()).isNotNull();
            softly.assertThat(tripResponse.route()).isNotNull();
            softly.assertThat(tripResponse.status()).isNotNull();
        });
    }

    @Test
    void 특정_위치정보를_삭제한다() {
        // given
        TripCreateResponse tripResponse = createTripAndGetResponse(huchuToken);
        PointResponse pointResponse = addPointAndGetResponse(pointCreateRequest(tripResponse.tripId()), huchuToken);

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .param("tripId", tripResponse.tripId())
                .when().delete("/points/{pointId}", pointResponse.pointId())
                .then().log().all()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    void 특정_위치정보_삭제시_인증에_실패하면_예외를_발생시킨다() {
        // given
        TripCreateResponse tripResponse = createTripAndGetResponse(huchuToken);
        PointResponse pointResponse = addPointAndGetResponse(pointCreateRequest(tripResponse.tripId()), huchuToken);

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .param("tripId", tripResponse.tripId())
                .when().delete("/points/{pointId}", pointResponse.pointId())
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 특정_회원의_전체_여행을_조회한다() {
        // given
        Long tripId = createTripAndGetResponse(huchuToken).tripId();
        updateTrip(tripUpdateRequest(), tripId, huchuToken);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/trips/me")
                .then().log().all()
                .extract();

        // then
        TripsSearchResponseOfMember tripsSearchResponseOfMember = response.as(TripsSearchResponseOfMember.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(tripsSearchResponseOfMember).usingRecursiveComparison().isEqualTo(
                    new TripsSearchResponseOfMember(
                            List.of(new TripSearchResponseOfMember(
                                            tripId,
                                            "제주도 여행",
                                            "",
                                            ""
                                    )
                            )
                    )
            );
        });
    }

    @Test
    void 여행의_이름과_상태를_수정한다() {
        // given
        Long tripId = createTripAndGetResponse(huchuToken).tripId();
        TripUpdateRequest tripUpdateRequest = new TripUpdateRequest("제주도 여행", FINISHED);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .body(tripUpdateRequest)
                .when().patch("/trips/{tripId}", tripId)
                .then().log().all()
                .extract();

        // then
        TripResponse tripResponse = searchTripAndGetResponse(tripId, huchuToken);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
            softly.assertThat(tripResponse.name()).isEqualTo("제주도 여행");
            softly.assertThat(tripResponse.status()).isEqualTo(FINISHED);
        });
    }

    @Test
    void 위치_정보를_조회한다() {
        // given
        TripCreateResponse tripResponse = createTripAndGetResponse(huchuToken);
        Long pointId = addPointAndGetResponse(pointCreateRequest(tripResponse.tripId()), huchuToken).pointId();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .param("tripId", tripResponse.tripId())
                .when().get("/points/{pointId}", pointId)
                .then().log().all()
                .extract();

        // then
        PointResponse pointResponse = response.as(PointResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(pointResponse).usingRecursiveComparison().isEqualTo(
                    new PointResponse(
                            pointId,
                            1.1,
                            2.2,
                            false,
                            LocalDateTime.of(2023, 7, 18, 20, 24)
                    )
            );
        });
    }

    @Test
    void 여행을_삭제한다() {
        // given
        TripCreateResponse tripResponse = createTripAndGetResponse(huchuToken);
        Long tripId = tripResponse.tripId();

        // expect
        RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .when().delete("/trips/{tripId}", tripId)
                .then().log().all()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    void 여행을_삭제할_때_인증에_실패하면_예외가_발생한다() {
        // given
        TripCreateResponse tripResponse = createTripAndGetResponse(huchuToken);
        Long tripId = tripResponse.tripId();

        // expect
        RestAssured.given().log().all()
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .when().delete("/trips/{tripId}", tripId)
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }
}
