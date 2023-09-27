package dev.tripdraw.test.step;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.tripdraw.trip.dto.PointCreateRequest;
import dev.tripdraw.trip.dto.PointResponse;
import dev.tripdraw.trip.dto.TripCreateResponse;
import dev.tripdraw.trip.dto.TripResponse;
import dev.tripdraw.trip.dto.TripUpdateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TripStep {

    public static ExtractableResponse<Response> createTrip(String token) {
        return RestAssured.given().log().all()
                .auth().preemptive().oauth2(token)
                .when().post("/trips")
                .then().log().all()
                .extract();
    }

    public static TripCreateResponse createTripAndGetResponse(String token) {
        ExtractableResponse<Response> response = createTrip(token);
        return response.as(TripCreateResponse.class);
    }

    public static ExtractableResponse<Response> addPoint(PointCreateRequest request, String token) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(token)
                .body(request)
                .when().post("/points")
                .then().log().all()
                .extract();
    }

    public static PointResponse addPointAndGetResponse(PointCreateRequest request, String token) {
        ExtractableResponse<Response> response = addPoint(request, token);
        return response.as(PointResponse.class);
    }

    public static ExtractableResponse<Response> updateTrip(TripUpdateRequest request, Long tripId, String token) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(token)
                .body(request)
                .when().patch("/trips/{tripId}", tripId)
                .then().log().all()
                .extract();
    }

    public static TripResponse searchTripAndGetResponse(Long tripId, String token) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(token)
                .when().get("/trips/{tripId}", tripId)
                .then().log().all()
                .extract();
        return response.as(TripResponse.class);
    }
}
