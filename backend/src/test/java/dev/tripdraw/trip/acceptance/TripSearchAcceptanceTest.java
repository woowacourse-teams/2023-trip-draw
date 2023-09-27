package dev.tripdraw.trip.acceptance;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.test.fixture.TestFixture.서울_2022_1_2_일;
import static dev.tripdraw.test.fixture.TestFixture.서울_2023_1_1_일;
import static dev.tripdraw.test.fixture.TestFixture.양양_2021_3_2_화;
import static dev.tripdraw.test.fixture.TestFixture.제주_2023_1_1_일;
import static dev.tripdraw.test.fixture.TestFixture.제주_2023_2_1_수;
import static dev.tripdraw.test.fixture.TripSearchQueryParamsFixture.addressAndLimitParams;
import static dev.tripdraw.test.fixture.TripSearchQueryParamsFixture.daysOfWeekAndLimitParams;
import static dev.tripdraw.test.fixture.TripSearchQueryParamsFixture.lastViewedIdAndLimitParams;
import static dev.tripdraw.test.fixture.TripSearchQueryParamsFixture.limitParams;
import static dev.tripdraw.test.fixture.TripSearchQueryParamsFixture.monthsAndLimitParams;
import static dev.tripdraw.test.fixture.TripSearchQueryParamsFixture.yearsAndLimitParams;
import static dev.tripdraw.test.step.PostStep.createPostAtCurrentPoint;
import static dev.tripdraw.test.step.TripStep.createTripAndGetResponse;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import dev.tripdraw.auth.application.JwtTokenProvider;
import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.test.ControllerTest;
import dev.tripdraw.trip.dto.TripSearchResponse;
import dev.tripdraw.trip.dto.TripsSearchResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
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
        Member reo = memberRepository.save(new Member("리오", "kakaoId", KAKAO));
        Member herb = memberRepository.save(new Member("허브", "kakaoId", KAKAO));

        huchuToken = jwtTokenProvider.generateAccessToken(huchu.id().toString());
        String reoToken = jwtTokenProvider.generateAccessToken(reo.id().toString());
        String herbToken = jwtTokenProvider.generateAccessToken(herb.id().toString());

        후추_양양_2021_3_2_화 = createTripAndGetResponse(huchuToken).tripId();
        createPostAtCurrentPoint(양양_2021_3_2_화(후추_양양_2021_3_2_화), huchuToken);

        후추_서울_2022_1_2_일 = createTripAndGetResponse(huchuToken).tripId();
        createPostAtCurrentPoint(서울_2022_1_2_일(후추_서울_2022_1_2_일), huchuToken);

        리오_제주_2023_1_1_일 = createTripAndGetResponse(reoToken).tripId();
        createPostAtCurrentPoint(제주_2023_1_1_일(리오_제주_2023_1_1_일), reoToken);

        리오_서울_2023_1_1_일 = createTripAndGetResponse(reoToken).tripId();
        createPostAtCurrentPoint(서울_2023_1_1_일(리오_서울_2023_1_1_일), reoToken);

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
                var tripSearchParams = limitParams(limit);

                // when
                ExtractableResponse<Response> response = RestAssured.given().log().all()
                        .auth().preemptive().oauth2(huchuToken)
                        .params(tripSearchParams)
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
                var tripSearchParams = limitParams(limit);

                // when
                ExtractableResponse<Response> response = RestAssured.given().log().all()
                        .auth().preemptive().oauth2(huchuToken)
                        .params(tripSearchParams)
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
                var tripSearchParams = limitParams(limit);

                // when
                ExtractableResponse<Response> response = RestAssured.given().log().all()
                        .auth().preemptive().oauth2(huchuToken)
                        .params(tripSearchParams)
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
                var tripSearchParams = limitParams(limit);

                // when
                ExtractableResponse<Response> response = RestAssured.given().log().all()
                        .auth().preemptive().oauth2(huchuToken)
                        .params(tripSearchParams)
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
                var tripSearchParams = limitParams(LIMIT);

                // when
                ExtractableResponse<Response> response = RestAssured.given().log().all()
                        .auth().preemptive().oauth2(huchuToken)
                        .params(tripSearchParams)
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
                var tripSearchParams = lastViewedIdAndLimitParams(lastViewedId, LIMIT);

                // when
                ExtractableResponse<Response> response = RestAssured.given().log().all()
                        .auth().preemptive().oauth2(huchuToken)
                        .params(tripSearchParams)
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

        private static final int LIMIT = 10;

        @Test
        void 조건없이_조회할_수_있다() {
            // given
            var tripSearchParams = limitParams(LIMIT);

            // when
            ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .auth().preemptive().oauth2(huchuToken)
                    .params(tripSearchParams)
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
            var tripSearchParams = yearsAndLimitParams(Set.of(2023), LIMIT);

            // when
            ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .auth().preemptive().oauth2(huchuToken)
                    .params(tripSearchParams)
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
            var tripSearchParams = monthsAndLimitParams(Set.of(1, 2), LIMIT);

            // when
            ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .auth().preemptive().oauth2(huchuToken)
                    .params(tripSearchParams)
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
            var tripSearchParams = daysOfWeekAndLimitParams(Set.of(1, 3), LIMIT);

            // when
            ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .auth().preemptive().oauth2(huchuToken)
                    .params(tripSearchParams)
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
            var tripSearchParams = addressAndLimitParams("seoul_songpa", LIMIT);

            // when
            ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .auth().preemptive().oauth2(huchuToken)
                    .params(tripSearchParams)
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
            Map<String, Object> params = Map.of(
                    "years", Set.of(2023, 2021),
                    "daysOfWeek", Set.of(1),
                    "address", "seoul",
                    "limit", LIMIT
            );

            // when
            ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .auth().preemptive().oauth2(huchuToken)
                    .params(params)
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
            var tripSearchParams = limitParams(LIMIT);

            // expect
            RestAssured.given().log().all()
                    .auth().preemptive().oauth2(WRONG_TOKEN)
                    .params(tripSearchParams)
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
