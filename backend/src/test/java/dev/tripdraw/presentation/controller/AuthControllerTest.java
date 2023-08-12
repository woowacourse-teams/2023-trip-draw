package dev.tripdraw.presentation.controller;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

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

    @Nested
    class 로그인할_때 {

        @Test
        void 가입된_회원이면_토큰이_포함된_응답을_반환한다() {
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
        void 신규_회원이면_회원을_저장하고_빈_토큰이_포함된_응답을_반환한다() {
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
    }

    @Nested
    class 신규_회원의_닉네임을_등록할_때 {

        @Test
        void 정상_등록하면_토큰이_포함된_응답을_반환한다() {
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
        void 회원이_존재하지_않으면_예외가_발생한다() {
            // given
            RegisterRequest registerRequest = new RegisterRequest("저장안된후추", KAKAO, "oauth.kakao.token");

            // expect
            RestAssured.given().log().all()
                    .contentType(APPLICATION_JSON_VALUE)
                    .body(registerRequest)
                    .when().post("/oauth/register")
                    .then().log().all()
                    .statusCode(NOT_FOUND.value());
        }

        @Test
        void 이미_존재하는_닉네임이면_예외가_발생한다() {
            // given
            Member member = memberRepository.save(new Member("중복닉네임", "kakaoId", KAKAO));
            RegisterRequest registerRequest = new RegisterRequest(member.nickname(), KAKAO, "oauth.kakao.token");

            // expect
            RestAssured.given().log().all()
                    .contentType(APPLICATION_JSON_VALUE)
                    .body(registerRequest)
                    .when().post("/oauth/register")
                    .then().log().all()
                    .statusCode(CONFLICT.value());
        }

        @Test
        void 닉네임에_공백이_있으면_예외가_발생한다() {
            // given
            RegisterRequest registerRequest = new RegisterRequest("공  백", KAKAO, "oauth.kakao.token");

            // expect
            RestAssured.given().log().all()
                    .contentType(APPLICATION_JSON_VALUE)
                    .body(registerRequest)
                    .when().post("/oauth/register")
                    .then().log().all()
                    .statusCode(BAD_REQUEST.value());
        }
    }
}
