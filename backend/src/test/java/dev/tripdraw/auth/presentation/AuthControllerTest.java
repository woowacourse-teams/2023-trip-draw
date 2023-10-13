package dev.tripdraw.auth.presentation;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.test.fixture.AuthFixture.OAUTH_TOKEN;
import static dev.tripdraw.test.fixture.AuthFixture.만료된_토큰_생성용_ACCESS_TOKEN_설정;
import static dev.tripdraw.test.fixture.AuthFixture.만료된_토큰_생성용_REFRESH_TOKEN_설정;
import static dev.tripdraw.test.fixture.MemberFixture.OAUTH_아이디;
import static dev.tripdraw.test.fixture.MemberFixture.닉네임이_없는_사용자;
import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.tripdraw.auth.application.JwtTokenProvider;
import dev.tripdraw.auth.domain.RefreshToken;
import dev.tripdraw.auth.domain.RefreshTokenRepository;
import dev.tripdraw.auth.dto.OauthRequest;
import dev.tripdraw.auth.dto.OauthResponse;
import dev.tripdraw.auth.dto.RegisterRequest;
import dev.tripdraw.auth.dto.TokenRefreshRequest;
import dev.tripdraw.auth.oauth.OauthClientProvider;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.test.ControllerTest;
import dev.tripdraw.test.TestKakaoApiClient;
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
    private int port;

    @MockBean
    private OauthClientProvider oauthClientProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        given(oauthClientProvider.provide(KAKAO)).willReturn(new TestKakaoApiClient());
    }

    @Nested
    class 로그인할_때 {

        @Test
        void 가입된_회원이면_토큰이_포함된_응답을_반환한다() {
            // given
            memberRepository.save(사용자());
            OauthRequest oauthRequest = new OauthRequest(KAKAO, OAUTH_TOKEN);

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
                softly.assertThat(oauthResponse.refreshToken()).isNotNull();
            });
        }

        @Test
        void 신규_회원이면_회원을_저장하고_빈_토큰이_포함된_응답을_반환한다() {
            // given
            OauthRequest oauthRequest = new OauthRequest(KAKAO, OAUTH_TOKEN);

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
                softly.assertThat(oauthResponse.refreshToken()).isEmpty();
            });
        }
    }

    @Nested
    class 신규_회원의_닉네임을_등록할_때 {

        @Test
        void 정상_등록하면_토큰이_포함된_응답을_반환한다() {
            // given
            memberRepository.save(닉네임이_없는_사용자(OAUTH_아이디));
            RegisterRequest registerRequest = new RegisterRequest("통후추", KAKAO, OAUTH_TOKEN);

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
                softly.assertThat(oauthResponse.refreshToken()).isNotEmpty();
            });
        }

        @Test
        void 회원이_존재하지_않으면_예외가_발생한다() {
            // given
            RegisterRequest registerRequest = new RegisterRequest("저장안된후추", KAKAO, OAUTH_TOKEN);

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
            Member member = memberRepository.save(사용자());
            RegisterRequest registerRequest = new RegisterRequest(member.nickname(), KAKAO, OAUTH_TOKEN);

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

    @Nested
    class Refresh_토큰_재발급_시 {

        @Test
        void 만료기간이_남은_Refresh_토큰이면_Access_토큰과_Refresh_토큰을_재발급한다() {
            // given
            Member member = memberRepository.save(사용자());
            String refreshToken = jwtTokenProvider.generateRefreshToken();
            refreshTokenRepository.save(new RefreshToken(member.id(), refreshToken));
            TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(refreshToken);

            // when
            ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .contentType(APPLICATION_JSON_VALUE)
                    .body(tokenRefreshRequest)
                    .when().post("/oauth/refresh")
                    .then().log().all()
                    .extract();

            // then
            OauthResponse oauthResponse = response.as(OauthResponse.class);
            assertSoftly(softly -> {
                softly.assertThat(response.statusCode()).isEqualTo(OK.value());
                softly.assertThat(oauthResponse.accessToken()).isNotEmpty();
                softly.assertThat(oauthResponse.refreshToken()).isNotEmpty();
            });
        }

        @Test
        void 만료기간이_지난_Refresh_토큰이면_401_예외가_발생한다() {
            // given
            Member member = memberRepository.save(사용자());
            JwtTokenProvider expiredTokenProvider = new JwtTokenProvider(
                    만료된_토큰_생성용_ACCESS_TOKEN_설정(),
                    만료된_토큰_생성용_REFRESH_TOKEN_설정()
            );
            String expiredToken = expiredTokenProvider.generateRefreshToken();
            refreshTokenRepository.save(new RefreshToken(member.id(), expiredToken));
            TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(expiredToken);

            // expect
            RestAssured.given().log().all()
                    .contentType(APPLICATION_JSON_VALUE)
                    .body(tokenRefreshRequest)
                    .when().post("/oauth/refresh")
                    .then().log().all()
                    .statusCode(UNAUTHORIZED.value());
        }
    }
}
