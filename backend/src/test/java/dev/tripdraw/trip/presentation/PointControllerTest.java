package dev.tripdraw.trip.presentation;

import static dev.tripdraw.test.fixture.MemberFixture.새로운_사용자;
import static dev.tripdraw.test.fixture.PointFixture.새로운_위치정보;
import static dev.tripdraw.test.fixture.TripFixture.새로운_여행;
import static dev.tripdraw.trip.presentation.PointControllerTest.PointRequestFixture.위치정보_생성_요청;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.tripdraw.auth.application.JwtTokenProvider;
import dev.tripdraw.draw.application.RouteImageGenerator;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.test.ControllerTest;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.PointRepository;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.dto.PointCreateRequest;
import dev.tripdraw.trip.dto.PointCreateResponse;
import dev.tripdraw.trip.dto.PointResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class PointControllerTest extends ControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private RouteImageGenerator routeImageGenerator;

    private String huchuToken;
    private Trip huchuTrip;
    private String reoToken;
    private Trip reoTrip;

    @BeforeEach
    public void setUp() {
        super.setUp();

        Member huchu = memberRepository.save(새로운_사용자("통후추"));
        Member reo = memberRepository.save(새로운_사용자("리오"));
        huchuToken = jwtTokenProvider.generateAccessToken(huchu.id().toString());
        reoToken = jwtTokenProvider.generateAccessToken(reo.id().toString());
        huchuTrip = tripRepository.save(새로운_여행(huchu));
        reoTrip = tripRepository.save(새로운_여행(reo));
    }

    @Test
    void 여행에_위치_정보를_추가한다() {
        // given
        PointCreateRequest request = 위치정보_생성_요청(huchuTrip.id());

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
        PointCreateRequest request = 위치정보_생성_요청(huchuTrip.id());

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(reoToken)
                .body(request)
                .when().post("/points")
                .then().log().all()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void 특정_위치정보를_삭제한다() {
        // given
        Point point = pointRepository.save(새로운_위치정보(huchuTrip));

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .param("tripId", huchuTrip.id())
                .when().delete("/points/{pointId}", point.id())
                .then().log().all()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    void 특정_위치정보_삭제시_인증에_실패하면_예외를_발생시킨다() {
        // given
        Point point = pointRepository.save(새로운_위치정보(huchuTrip));

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(reoToken)
                .param("tripId", huchuTrip.id())
                .when().delete("/points/{pointId}", point.id())
                .then().log().all()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void 위치_정보를_조회한다() {
        // given
        Point point = pointRepository.save(새로운_위치정보(huchuTrip));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .param("tripId", huchuTrip.id())
                .when().get("/points/{pointId}", point.id())
                .then().log().all()
                .extract();

        // then
        PointResponse pointResponse = response.as(PointResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(pointResponse).usingRecursiveComparison()
                    .ignoringFieldsOfTypes(LocalDateTime.class)
                    .isEqualTo(PointResponse.from(point));
        });
    }

    static class PointRequestFixture {
        public static PointCreateRequest 위치정보_생성_요청(Long tripId) {
            return new PointCreateRequest(tripId, 1.1, 2.2, LocalDateTime.now());
        }
    }
}
