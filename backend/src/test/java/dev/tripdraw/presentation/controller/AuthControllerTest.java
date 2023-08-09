package dev.tripdraw.presentation.controller;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.tripdraw.application.oauth.OauthClientProvider;
import dev.tripdraw.common.TestKakaoApiClient;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.dto.auth.OauthRequest;
import dev.tripdraw.dto.auth.OauthResponse;
import dev.tripdraw.dto.auth.RegisterRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthControllerTest extends ControllerTest {

    @LocalServerPort
    int port;

    @MockBean
    OauthClientProvider oauthClientProvider;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        when(oauthClientProvider.provide(KAKAO))
                .thenReturn(new TestKakaoApiClient());
    }

    @Test
    void 가입된_회원이_카카오_소셜_로그인하면_토큰이_포함된_응답을_반환한다() {
        // given
        memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        OauthRequest oauthRequest = new OauthRequest(KAKAO, "oauth.kakao.token");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(oauthRequest)
                .when().post("/oauth/login")
                .then().log().all()
                .extract();

        // then
        OauthResponse oauthResponse = response.as(OauthResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(oauthResponse.accessToken()).isNotNull();
        });
    }

    @Test
    void 신규_회원이_로그인하면_회원을_저장하고_빈_토큰이_포함된_응답을_반환한다() {
        // given
        OauthRequest oauthRequest = new OauthRequest(KAKAO, "oauth.kakao.token");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(oauthRequest)
                .when().post("/oauth/login")
                .then().log().all()
                .extract();

        // then
        OauthResponse oauthResponse = response.as(OauthResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(oauthResponse.accessToken()).isEmpty();
        });
    }

    @Test
    void 신규_회원의_닉네임을_등록하면_토큰이_포함된_응답을_반환한다() {
        // given
        Member member = Member.of("kakaoId", KAKAO);
        memberRepository.save(member);

        RegisterRequest registerRequest = new RegisterRequest("통후추", KAKAO, "oauth.kakao.token");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(registerRequest)
                .when().post("/oauth/register")
                .then().log().all()
                .extract();

        // then
        OauthResponse oauthResponse = response.as(OauthResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(oauthResponse.accessToken()).isNotEmpty();
        });
    }

    @Test
    void 신규_회원의_닉네임을_등록할_때_회원이_존재하지_않으면_예외가_발생한다() {
        // given
        RegisterRequest registerRequest = new RegisterRequest("저장안된후추", KAKAO, "oauth.kakao.token");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(registerRequest)
                .when().post("/oauth/register")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
