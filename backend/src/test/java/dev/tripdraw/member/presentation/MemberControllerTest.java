package dev.tripdraw.member.presentation;

import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import dev.tripdraw.auth.application.JwtTokenProvider;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.member.dto.MemberSearchResponse;
import dev.tripdraw.test.ControllerTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

class MemberControllerTest extends ControllerTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    void 사용자를_조회한다() {
        // given
        Member member = memberRepository.save(사용자());
        String accessToken = jwtTokenProvider.generateAccessToken(member.id().toString());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .extract();

        // then
        MemberSearchResponse memberSearchResponse = response.as(MemberSearchResponse.class);
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(memberSearchResponse)
                    .usingRecursiveComparison()
                    .isEqualTo(new MemberSearchResponse(member.id(), member.nickname()));
        });
    }

    @Test
    void 사용자를_삭제한다() {
        // given
        Member member = memberRepository.save(사용자());
        String accessToken = jwtTokenProvider.generateAccessToken(member.id().toString());

        // expect
        RestAssured.given().log().all()
                .auth().preemptive().oauth2(accessToken)
                .when().delete("/members/me")
                .then().log().all()
                .statusCode(NO_CONTENT.value());
    }
}
