package dev.tripdraw.presentation.controller;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static java.lang.Long.MIN_VALUE;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import dev.tripdraw.application.oauth.AuthTokenManager;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.dto.member.MemberSearchResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberControllerTest extends ControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthTokenManager authTokenManager;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void code를_입력_받아_사용자를_조회한다() {
        // given
        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        String code = authTokenManager.generate(member.id());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("code", code)
                .when().get("/members")
                .then().log().all()
                .extract();

        // then
        MemberSearchResponse memberSearchResponse = response.as(MemberSearchResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(OK.value());
            softly.assertThat(memberSearchResponse).usingRecursiveComparison().isEqualTo(
                    new MemberSearchResponse(member.id(), "통후추")
            );
        });
    }

    @Test
    void code를_입력_받아_사용자를_조회할_때_존재하지_않는_사용자라면_예외가_발생한다() {
        // given
        String code = authTokenManager.generate(MIN_VALUE);

        // expect
        RestAssured.given().log().all()
                .param("code", code)
                .when().get("/members")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void code를_입력_받아_사용자를_조회할_때_이미_삭제된_사용자라면_예외가_발생한다() {
        // given
        Member member = memberRepository.save(new Member("순후추", "kakaoId", KAKAO));
        String code = authTokenManager.generate(member.id());

        memberRepository.delete(member);

        // expect
        RestAssured.given().log().all()
                .param("code", code)
                .when().get("/members")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void code를_입력_받아_사용자를_삭제한다() {
        // given
        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        String code = authTokenManager.generate(member.id());

        // expect
        RestAssured.given().log().all()
                .param("code", code)
                .when().delete("/members")
                .then().log().all()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    void code를_입력_받아_사용자를_삭제할_때_존재하지_않는_사용자라면_예외를_발생시킨다() {
        // given
        String code = authTokenManager.generate(MIN_VALUE);

        // expect
        RestAssured.given().log().all()
                .param("code", code)
                .when().delete("/members")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void code를_입력_받아_사용자를_삭제할_때_이미_삭제된_사용자라면_예외를_발생시킨다() {
        // given
        Member member = memberRepository.save(new Member("순후추", "kakaoId", KAKAO));
        String code = authTokenManager.generate(member.id());

        memberRepository.delete(member);

        // expect
        RestAssured.given().log().all()
                .param("code", code)
                .when().delete("/members")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }
}
