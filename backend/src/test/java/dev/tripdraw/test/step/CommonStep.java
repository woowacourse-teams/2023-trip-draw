package dev.tripdraw.test.step;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class CommonStep {

    public static ExtractableResponse<Response> GET_요청(String accessToken, String path) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .when().get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> POST_요청(String accessToken, Object body, String path) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .body(body)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> POST_요청(Object body, String path) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(body)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> PATCH_요청(String accessToken, Object body, String path) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .body(body)
                .when().patch(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> DELETE_요청(String accessToken, String path) {
        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(accessToken)
                .when().delete(path)
                .then().log().all()
                .extract();
    }
}
