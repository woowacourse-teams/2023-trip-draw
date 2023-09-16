package dev.tripdraw.trip.presentation;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.test.fixture.TestFixture.pointCreateRequest;
import static dev.tripdraw.test.fixture.TestFixture.tripUpdateRequest;
import static dev.tripdraw.test.fixture.TestFixture.서울_2022_1_2_일;
import static dev.tripdraw.test.fixture.TestFixture.서울_2023_1_1_일;
import static dev.tripdraw.test.fixture.TestFixture.양양_2021_3_2_화;
import static dev.tripdraw.test.fixture.TestFixture.제주_2023_1_1_일;
import static dev.tripdraw.test.fixture.TestFixture.제주_2023_2_1_수;
import static dev.tripdraw.test.fixture.TripSearchConditionsFixture.addressTripSearchConditions;
import static dev.tripdraw.test.fixture.TripSearchConditionsFixture.daysOfWeekTripSearchConditions;
import static dev.tripdraw.test.fixture.TripSearchConditionsFixture.emptyTripSearchConditions;
import static dev.tripdraw.test.fixture.TripSearchConditionsFixture.monthsTripSearchConditions;
import static dev.tripdraw.test.fixture.TripSearchConditionsFixture.yearsTripSearchConditions;
import static dev.tripdraw.test.step.PostStep.createPostAtCurrentPoint;
import static dev.tripdraw.test.step.TripStep.addPointAndGetResponse;
import static dev.tripdraw.test.step.TripStep.createTripAndGetResponse;
import static dev.tripdraw.test.step.TripStep.searchTripAndGetResponse;
import static dev.tripdraw.test.step.TripStep.updateTrip;
import static dev.tripdraw.trip.domain.TripStatus.FINISHED;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.tripdraw.auth.application.JwtTokenProvider;
import dev.tripdraw.common.dto.SearchPaging;
import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.test.ControllerTest;
import dev.tripdraw.trip.dto.PointCreateRequest;
import dev.tripdraw.trip.dto.PointCreateResponse;
import dev.tripdraw.trip.dto.PointResponse;
import dev.tripdraw.trip.dto.TripCreateResponse;
import dev.tripdraw.trip.dto.TripResponse;
import dev.tripdraw.trip.dto.TripSearchConditions;
import dev.tripdraw.trip.dto.TripSearchRequest;
import dev.tripdraw.trip.dto.TripSearchResponse;
import dev.tripdraw.trip.dto.TripSearchResponseOfMember;
import dev.tripdraw.trip.dto.TripUpdateRequest;
import dev.tripdraw.trip.dto.TripsSearchResponse;
import dev.tripdraw.trip.dto.TripsSearchResponseOfMember;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
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
    private String leoToken;
    private String herbToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        Member huchu = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        Member leo = memberRepository.save(new Member("리오", "kakaoId", KAKAO));
        Member herb = memberRepository.save(new Member("허브", "kakaoId", KAKAO));

        huchuToken = jwtTokenProvider.generateAccessToken(huchu.id().toString());
        leoToken = jwtTokenProvider.generateAccessToken(leo.id().toString());
        herbToken = jwtTokenProvider.generateAccessToken(herb.id().toString());
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
                .when().get("/trips/mine")
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

    @Nested
    class 여러_회원이_여행에_감상을_남긴_후 {

        private Long lastViewedId;
        private Long 후추_양양_2021_3_2_화;
        private Long 후추_서울_2022_1_2_일;
        private Long 리오_제주_2023_1_1_일;
        private Long 리오_서울_2023_1_1_일;
        private Long 허브_제주_2023_2_1_수;

        @BeforeEach
        void setUp() {
            후추_양양_2021_3_2_화 = createTripAndGetResponse(huchuToken).tripId();
            createPostAtCurrentPoint(양양_2021_3_2_화(후추_양양_2021_3_2_화), huchuToken);

            후추_서울_2022_1_2_일 = createTripAndGetResponse(huchuToken).tripId();
            createPostAtCurrentPoint(서울_2022_1_2_일(후추_서울_2022_1_2_일), huchuToken);

            리오_제주_2023_1_1_일 = createTripAndGetResponse(leoToken).tripId();
            createPostAtCurrentPoint(제주_2023_1_1_일(리오_제주_2023_1_1_일), leoToken);

            리오_서울_2023_1_1_일 = createTripAndGetResponse(leoToken).tripId();
            createPostAtCurrentPoint(서울_2023_1_1_일(리오_서울_2023_1_1_일), leoToken);

            허브_제주_2023_2_1_수 = createTripAndGetResponse(herbToken).tripId();
            createPostAtCurrentPoint(제주_2023_2_1_수(허브_제주_2023_2_1_수), herbToken);

            lastViewedId = 허브_제주_2023_2_1_수;
        }

        @Nested
        class 감상이_있는_모든_여행을_페이지네이션으로_조회할_때 {

            @Nested
            class 조회할_수_있는_여행_수_미만으로_개수_제한을_입력하면 {

                @ParameterizedTest
                @CsvSource({"1, 1", "4, 4"})
                void 해당_개수만큼_여행이_조회된다(int limit, int expectedSize) {
                    // given
                    var tripSearchRequest = new TripSearchRequest(
                            emptyTripSearchConditions(),
                            new SearchPaging(null, limit)
                    );

                    // when
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .auth().preemptive().oauth2(huchuToken)
                            .contentType(APPLICATION_JSON_VALUE)
                            .body(tripSearchRequest)
                            .when().get("/trips")
                            .then().log().all()
                            .extract();

                    // then
                    TripsSearchResponse tripsSearchResponse = response.as(TripsSearchResponse.class);

                    assertSoftly(softly -> {
                        softly.assertThat(response.statusCode()).isEqualTo(OK.value());
                        softly.assertThat(tripsSearchResponse.trips()).hasSize(expectedSize);
                    });
                }

                @ParameterizedTest
                @ValueSource(ints = {1, 4})
                void 다음_페이지가_존재한다(int limit) {
                    // given
                    var tripSearchRequest = new TripSearchRequest(
                            emptyTripSearchConditions(),
                            new SearchPaging(null, limit)
                    );

                    // when
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .auth().preemptive().oauth2(huchuToken)
                            .contentType(APPLICATION_JSON_VALUE)
                            .body(tripSearchRequest)
                            .when().get("/trips")
                            .then().log().all()
                            .extract();

                    // then
                    TripsSearchResponse tripsSearchResponse = response.as(TripsSearchResponse.class);

                    assertSoftly(softly -> {
                        softly.assertThat(response.statusCode()).isEqualTo(OK.value());
                        softly.assertThat(tripsSearchResponse.hasNextPage()).isTrue();
                    });
                }
            }

            @Nested
            class 조회할_수_있는_여행_수_이상으로_개수_제한을_입력하면 {

                @ParameterizedTest
                @CsvSource({"5, 5", "6, 5"})
                void 모든_여행이_조회된다(int limit, int expectedSize) {
                    // given
                    var tripSearchRequest = new TripSearchRequest(
                            emptyTripSearchConditions(),
                            new SearchPaging(null, limit)
                    );

                    // when
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .auth().preemptive().oauth2(huchuToken)
                            .contentType(APPLICATION_JSON_VALUE)
                            .body(tripSearchRequest)
                            .when().get("/trips")
                            .then().log().all()
                            .extract();

                    // then
                    TripsSearchResponse tripsSearchResponse = response.as(TripsSearchResponse.class);

                    assertSoftly(softly -> {
                        softly.assertThat(response.statusCode()).isEqualTo(OK.value());
                        softly.assertThat(tripsSearchResponse.trips()).hasSize(expectedSize);
                    });
                }

                @ParameterizedTest
                @ValueSource(ints = {5, 6})
                void 다음_페이지가_존재하지_않는다(int limit) {
                    // given
                    var tripSearchRequest = new TripSearchRequest(
                            emptyTripSearchConditions(),
                            new SearchPaging(null, limit)
                    );

                    // when
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .auth().preemptive().oauth2(huchuToken)
                            .contentType(APPLICATION_JSON_VALUE)
                            .body(tripSearchRequest)
                            .when().get("/trips")
                            .then().log().all()
                            .extract();

                    // then
                    TripsSearchResponse tripsSearchResponse = response.as(TripsSearchResponse.class);

                    assertSoftly(softly -> {
                        softly.assertThat(response.statusCode()).isEqualTo(OK.value());
                        softly.assertThat(tripsSearchResponse.hasNextPage()).isFalse();
                    });
                }
            }

            @Nested
            class 마지막으로_조회한_여행_ID를 {

                private static final int LIMIT = 10;

                @Test
                void 입력하지_않으면_최신_여행부터_내림차순으로_조회된다() {
                    // given
                    var tripSearchRequest = new TripSearchRequest(
                            emptyTripSearchConditions(),
                            new SearchPaging(null, LIMIT)
                    );

                    // when
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .auth().preemptive().oauth2(huchuToken)
                            .contentType(APPLICATION_JSON_VALUE)
                            .body(tripSearchRequest)
                            .when().get("/trips")
                            .then().log().all()
                            .extract();

                    // then
                    TripsSearchResponse tripsSearchResponse = response.as(TripsSearchResponse.class);

                    assertSoftly(softly -> {
                        softly.assertThat(response.statusCode()).isEqualTo(OK.value());
                        softly.assertThat(searchedTripIds(tripsSearchResponse)).containsExactly(
                                허브_제주_2023_2_1_수,
                                리오_서울_2023_1_1_일,
                                리오_제주_2023_1_1_일,
                                후추_서울_2022_1_2_일,
                                후추_양양_2021_3_2_화
                        );
                    });
                }

                @Test
                void 입력하면_해당_여행_이하로_최신_여행을_내림차순으로_조회된다() {
                    // given
                    var tripSearchRequest = new TripSearchRequest(
                            emptyTripSearchConditions(),
                            new SearchPaging(lastViewedId, LIMIT)
                    );

                    // when
                    ExtractableResponse<Response> response = RestAssured.given().log().all()
                            .auth().preemptive().oauth2(huchuToken)
                            .contentType(APPLICATION_JSON_VALUE)
                            .body(tripSearchRequest)
                            .when().get("/trips")
                            .then().log().all()
                            .extract();

                    // then
                    TripsSearchResponse tripsSearchResponse = response.as(TripsSearchResponse.class);

                    assertSoftly(softly -> {
                        softly.assertThat(response.statusCode()).isEqualTo(OK.value());
                        softly.assertThat(searchedTripIds(tripsSearchResponse)).containsExactly(
                                리오_서울_2023_1_1_일,
                                리오_제주_2023_1_1_일,
                                후추_서울_2022_1_2_일,
                                후추_양양_2021_3_2_화
                        );
                    });
                }

            }
        }

        private List<Long> searchedTripIds(TripsSearchResponse tripsSearchResponse) {
            return tripsSearchResponse.trips().stream()
                    .map(TripSearchResponse::tripId)
                    .toList();
        }

        @Nested
        class 감상이_있는_모든_여행을_조건에_따라_조회할_때 {

            private final SearchPaging nullLastViewedIdAnd10Limit = new SearchPaging(null, 10);

            @Test
            void 조건없이_조회할_수_있다() {
                // given
                var tripSearchRequest = new TripSearchRequest(
                        emptyTripSearchConditions(),
                        nullLastViewedIdAnd10Limit
                );

                // when
                ExtractableResponse<Response> response = RestAssured.given().log().all()
                        .auth().preemptive().oauth2(huchuToken)
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(tripSearchRequest)
                        .when().get("/trips")
                        .then().log().all()
                        .extract();

                // then
                TripsSearchResponse tripsSearchResponse = response.as(TripsSearchResponse.class);

                assertSoftly(softly -> {
                    softly.assertThat(response.statusCode()).isEqualTo(OK.value());
                    softly.assertThat(searchedTripIds(tripsSearchResponse)).containsExactly(
                            허브_제주_2023_2_1_수,
                            리오_서울_2023_1_1_일,
                            리오_제주_2023_1_1_일,
                            후추_서울_2022_1_2_일,
                            후추_양양_2021_3_2_화
                    );
                });
            }

            @Test
            void 연도를_조건으로_조회할_수_있다() {
                // given
                var tripSearchRequest = new TripSearchRequest(
                        yearsTripSearchConditions(Set.of(2023)),
                        nullLastViewedIdAnd10Limit
                );

                // when
                ExtractableResponse<Response> response = RestAssured.given().log().all()
                        .auth().preemptive().oauth2(huchuToken)
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(tripSearchRequest)
                        .when().get("/trips")
                        .then().log().all()
                        .extract();

                // then
                TripsSearchResponse tripsSearchResponse = response.as(TripsSearchResponse.class);

                assertSoftly(softly -> {
                    softly.assertThat(response.statusCode()).isEqualTo(OK.value());
                    softly.assertThat(searchedTripIds(tripsSearchResponse)).containsExactly(
                            허브_제주_2023_2_1_수,
                            리오_서울_2023_1_1_일,
                            리오_제주_2023_1_1_일
                    );
                });
            }

            @Test
            void 월을_조건으로_조회할_수_있다() {
                // given
                var tripSearchRequest = new TripSearchRequest(
                        monthsTripSearchConditions(Set.of(1, 2)),
                        nullLastViewedIdAnd10Limit
                );

                // when
                ExtractableResponse<Response> response = RestAssured.given().log().all()
                        .auth().preemptive().oauth2(huchuToken)
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(tripSearchRequest)
                        .when().get("/trips")
                        .then().log().all()
                        .extract();

                // then
                TripsSearchResponse tripsSearchResponse = response.as(TripsSearchResponse.class);

                assertSoftly(softly -> {
                    softly.assertThat(response.statusCode()).isEqualTo(OK.value());
                    softly.assertThat(searchedTripIds(tripsSearchResponse)).containsExactly(
                            허브_제주_2023_2_1_수,
                            리오_서울_2023_1_1_일,
                            리오_제주_2023_1_1_일,
                            후추_서울_2022_1_2_일
                    );
                });
            }

            @Test
            void 요일을_조건으로_조회할_수_있다() {
                // given
                var tripSearchRequest = new TripSearchRequest(
                        daysOfWeekTripSearchConditions(Set.of(1, 3)),
                        nullLastViewedIdAnd10Limit
                );

                // when
                ExtractableResponse<Response> response = RestAssured.given().log().all()
                        .auth().preemptive().oauth2(huchuToken)
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(tripSearchRequest)
                        .when().get("/trips")
                        .then().log().all()
                        .extract();

                // then
                TripsSearchResponse tripsSearchResponse = response.as(TripsSearchResponse.class);

                assertSoftly(softly -> {
                    softly.assertThat(response.statusCode()).isEqualTo(OK.value());
                    softly.assertThat(searchedTripIds(tripsSearchResponse)).containsExactly(
                            리오_서울_2023_1_1_일,
                            리오_제주_2023_1_1_일,
                            후추_서울_2022_1_2_일,
                            후추_양양_2021_3_2_화
                    );
                });
            }

            @Test
            void 주소를_조건으로_조회할_수_있다() {
                // given
                var tripSearchRequest = new TripSearchRequest(
                        addressTripSearchConditions("seoul_songpa"),
                        nullLastViewedIdAnd10Limit
                );

                // when
                ExtractableResponse<Response> response = RestAssured.given().log().all()
                        .auth().preemptive().oauth2(huchuToken)
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(tripSearchRequest)
                        .when().get("/trips")
                        .then().log().all()
                        .extract();

                // then
                TripsSearchResponse tripsSearchResponse = response.as(TripsSearchResponse.class);

                assertSoftly(softly -> {
                    softly.assertThat(response.statusCode()).isEqualTo(OK.value());
                    softly.assertThat(searchedTripIds(tripsSearchResponse)).containsExactly(
                            리오_서울_2023_1_1_일,
                            후추_서울_2022_1_2_일
                    );
                });
            }

            @Test
            void 여러_조건으로_조회할_수_있다() {
                // given
                var multiSearchConditions = new TripSearchConditions(
                        Set.of(2023, 2021),
                        Set.of(),
                        Set.of(1),
                        Set.of(),
                        Set.of(),
                        "seoul"
                );
                var tripSearchRequest = new TripSearchRequest(
                        multiSearchConditions,
                        nullLastViewedIdAnd10Limit
                );

                // when
                ExtractableResponse<Response> response = RestAssured.given().log().all()
                        .auth().preemptive().oauth2(huchuToken)
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(tripSearchRequest)
                        .when().get("/trips")
                        .then().log().all()
                        .extract();

                // then
                TripsSearchResponse tripsSearchResponse = response.as(TripsSearchResponse.class);

                assertSoftly(softly -> {
                    softly.assertThat(response.statusCode()).isEqualTo(OK.value());
                    softly.assertThat(searchedTripIds(tripsSearchResponse)).containsExactly(
                            리오_서울_2023_1_1_일
                    );
                });
            }

            @Test
            void 조건이_유효하지_않을_경우_예외를_발생시킨다() {
                // given
                var tripSearchRequest = new TripSearchRequest(
                        monthsTripSearchConditions(Set.of(Integer.MAX_VALUE)),
                        nullLastViewedIdAnd10Limit
                );

                // expect
                RestAssured.given().log().all()
                        .auth().preemptive().oauth2(huchuToken)
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(tripSearchRequest)
                        .when().get("/trips")
                        .then().log().all()
                        .statusCode(BAD_REQUEST.value());
            }

            @Test
            void 인증에_실패할_경우_예외를_발생시킨다() {
                // given
                var tripSearchRequest = new TripSearchRequest(
                        emptyTripSearchConditions(),
                        nullLastViewedIdAnd10Limit
                );

                // expect
                RestAssured.given().log().all()
                        .auth().preemptive().oauth2(WRONG_TOKEN)
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(tripSearchRequest)
                        .when().get("/trips")
                        .then().log().all()
                        .statusCode(UNAUTHORIZED.value());
            }
        }
    }
}
