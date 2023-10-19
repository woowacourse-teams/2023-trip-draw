package dev.tripdraw.trip.presentation;

import static dev.tripdraw.test.fixture.MemberFixture.새로운_사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.TestFixture.서울_2022_1_2_일;
import static dev.tripdraw.test.fixture.TestFixture.서울_2023_1_1_일;
import static dev.tripdraw.test.fixture.TestFixture.제주_2023_1_1_일;
import static dev.tripdraw.test.fixture.TripFixture.새로운_여행;
import static dev.tripdraw.test.step.PostStep.createPostAtCurrentPoint;
import static dev.tripdraw.test.step.TripStep.createTripAndGetResponse;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.OK;

import dev.tripdraw.auth.application.JwtTokenProvider;
import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.test.ControllerTest;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.dto.TripResponse;
import dev.tripdraw.trip.dto.v1.TripResponseV1;
import dev.tripdraw.trip.dto.v1.TripSearchResponseV1;
import dev.tripdraw.trip.dto.v1.TripsSearchResponseV1;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class TripControllerV1Test extends ControllerTest {

    private static final String VERSION_HEADER = "X-version";
    private static final String V2 = "V2";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private RouteImageGenerator routeImageGenerator;

    private String huchuToken;
    private Trip huchuTrip;
    private String reoToken;
    private Trip reoTrip;
    private Member huchu;
    private Member reo;

    @BeforeEach
    public void setUp() {
        super.setUp();

        huchu = memberRepository.save(새로운_사용자("통후추"));
        reo = memberRepository.save(새로운_사용자("리오"));
        huchuToken = jwtTokenProvider.generateAccessToken(huchu.id().toString());
        reoToken = jwtTokenProvider.generateAccessToken(reo.id().toString());

        Trip trip = 새로운_여행(huchu);
        Point point = 새로운_위치정보(trip);
        trip.add(point);

        huchuTrip = tripRepository.save(trip);
        reoTrip = tripRepository.save(새로운_여행(reo));
    }

    @Test
    void 여행을_ID로_조회한다() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/trips/{tripId}", huchuTrip.id())
                .then().log().all()
                .extract();

        // then
        TripResponseV1 tripResponseV1 = response.as(TripResponseV1.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(tripResponseV1).usingRecursiveComparison()
                    .ignoringFieldsOfTypes(LocalDateTime.class)
                    .isEqualTo(TripResponseV1.from(TripResponse.from(huchuTrip, huchu.id())));
        });
    }
    
    @Test
    void 감상이_있는_모든_여행을_조건으로_조회할_수_있다() {
        // given
        Long 후추_서울_2022_1_2_일 = createTripAndGetResponse(huchuToken).tripId();
        createPostAtCurrentPoint(서울_2022_1_2_일(후추_서울_2022_1_2_일), huchuToken);

        Long 리오_제주_2023_1_1_일 = createTripAndGetResponse(reoToken).tripId();
        createPostAtCurrentPoint(제주_2023_1_1_일(리오_제주_2023_1_1_일), reoToken);

        Long 리오_서울_2023_1_1_일 = createTripAndGetResponse(reoToken).tripId();
        createPostAtCurrentPoint(서울_2023_1_1_일(리오_서울_2023_1_1_일), reoToken);

        Map<String, Object> params = Map.of(
                "years", Set.of(2023, 2021),
                "daysOfWeek", Set.of(1),
                "address", "seoul",
                "limit", 10
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .params(params)
                .when().get("/trips")
                .then().log().all()
                .extract();

        // then
        TripsSearchResponseV1 tripsSearchResponseV1 = response.as(TripsSearchResponseV1.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(tripsSearchResponseV1.trips())
                    .extracting(TripSearchResponseV1::tripId)
                    .containsExactly(리오_서울_2023_1_1_일);
        });
    }
}
