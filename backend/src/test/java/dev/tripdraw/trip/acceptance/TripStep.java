package dev.tripdraw.trip.acceptance;

import static dev.tripdraw.test.step.CommonStep.POST_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TripStep {

    public static ExtractableResponse<Response> 여행_생성_요청(String 인증_토큰) {
        return POST_요청(인증_토큰, "/trips");
    }
}
