package dev.tripdraw.auth.acceptance;

import static dev.tripdraw.test.step.CommonStep.POST_요청;

import dev.tripdraw.auth.dto.OauthRequest;
import dev.tripdraw.auth.dto.OauthResponse;
import dev.tripdraw.common.auth.OauthType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SuppressWarnings("NonAsciiCharacters")
public class AuthStep {

    public static String 사용자_회원가입_후_토큰_반환(String nickname) {
        var response = 사용자_회원가입_요청(nickname);
        return 액세스_토큰_추출(response);
    }

    public static ExtractableResponse<Response> 사용자_회원가입_요청(String nickname) {
        var oauthRequest = new OauthRequest(OauthType.KAKAO, nickname);
        POST_요청(oauthRequest, "/oauth/login");
        return POST_요청(oauthRequest, "/oauth/register");
    }

    public static String 액세스_토큰_추출(ExtractableResponse<Response> response) {
        var oauthResponse = response.as(OauthResponse.class);
        return oauthResponse.accessToken();
    }
}
