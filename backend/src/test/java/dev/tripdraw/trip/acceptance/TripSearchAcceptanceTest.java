package dev.tripdraw.trip.acceptance;

import dev.tripdraw.auth.application.JwtTokenProvider;
import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.test.ControllerTest;
import dev.tripdraw.trip.dto.*;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Set;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.test.fixture.TestFixture.*;
import static dev.tripdraw.test.fixture.TripSearchConditionsFixture.*;
import static dev.tripdraw.test.step.PostStep.createPostAtCurrentPoint;
import static dev.tripdraw.test.step.TripStep.createTripAndGetResponse;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TripSearchAcceptanceTest extends ControllerTest {

    private static final String WRONG_TOKEN = "wrong.long.token";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private RouteImageGenerator routeImageGenerator;

    private String huchuToken;
    private Long lastViewedId;
    private Long 후추_양양_2021_3_2_화;
    private Long 후추_서울_2022_1_2_일;
    private Long 리오_제주_2023_1_1_일;
    private Long 리오_서울_2023_1_1_일;
    private Long 허브_제주_2023_2_1_수;

    @BeforeEach
    public void setUp() {
        super.setUp();

        Member huchu = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        Member leo = memberRepository.save(new Member("리오", "kakaoId", KAKAO));
        Member herb = memberRepository.save(new Member("허브", "kakaoId", KAKAO));

        huchuToken = jwtTokenProvider.generateAccessToken(huchu.id().toString());
        String leoToken = jwtTokenProvider.generateAccessToken(leo.id().toString());
        String herbToken = jwtTokenProvider.generateAccessToken(herb.id().toString());

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
        class 개수_제한을 {

            @ParameterizedTest
            @CsvSource({"1, 1", "4, 4"})
            void 조회할_수_있는_여행_수_미만으로_입력하면_해당_개수만큼_여행이_조회된다(int limit, int expectedSize) {
                // given
                var tripSearchRequest = new TripSearchRequest(
                        emptyTripSearchConditions(),
                        new TripSearchPaging(null, limit)
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
            void 조회할_수_있는_여행_수_미만으로_입력하면_다음_페이지가_존재한다(int limit) {
                // given
                var tripSearchRequest = new TripSearchRequest(
                        emptyTripSearchConditions(),
                        new TripSearchPaging(null, limit)
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

            @ParameterizedTest
            @CsvSource({"5, 5", "6, 5"})
            void 조회할_수_있는_여행_수_이상으로_입력하면_모든_여행이_조회된다(int limit, int expectedSize) {
                // given
                var tripSearchRequest = new TripSearchRequest(
                        emptyTripSearchConditions(),
                        new TripSearchPaging(null, limit)
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
            void 조회할_수_있는_여행_수_이상으로_입력하면_다음_페이지가_존재하지_않는다(int limit) {
                // given
                var tripSearchRequest = new TripSearchRequest(
                        emptyTripSearchConditions(),
                        new TripSearchPaging(null, limit)
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
                        new TripSearchPaging(null, LIMIT)
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
                        new TripSearchPaging(lastViewedId, LIMIT)
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

    @Nested
    class 감상이_있는_모든_여행을_조건에_따라_조회할_때 {

        private final TripSearchPaging nullLastViewedIdAnd10Limit = new TripSearchPaging(null, 10);

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

    private List<Long> searchedTripIds(TripsSearchResponse tripsSearchResponse) {
        return tripsSearchResponse.trips().stream()
                .map(TripSearchResponse::tripId)
                .toList();
    }
}
