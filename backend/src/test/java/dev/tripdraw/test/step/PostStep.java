package dev.tripdraw.test.step;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import dev.tripdraw.post.dto.PostAndPointCreateRequest;
import dev.tripdraw.post.dto.PostCreateResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class PostStep {

    public static ExtractableResponse<Response> createPostAtCurrentPoint(PostAndPointCreateRequest request,
                                                                         String token) {
        return RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(token)
                .multiPart("dto", request, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .extract();
    }

    public static PostCreateResponse createPostAtCurrentPointAndGetResponse(PostAndPointCreateRequest request,
                                                                            String token) {
        ExtractableResponse<Response> response = createPostAtCurrentPoint(request, token);
        return response.as(PostCreateResponse.class);
    }
}
