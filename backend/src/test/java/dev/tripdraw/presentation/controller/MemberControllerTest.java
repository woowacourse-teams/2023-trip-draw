package dev.tripdraw.presentation.controller;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
    void 사용자를_조회한다() {
        // given
        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/members/{memberId}", member.id())
                .then().log().all()
                .statusCode(OK.value())
                .extract();
        MemberSearchResponse memberSearchResponse = response.as(MemberSearchResponse.class);

        // expect
        assertThat(memberSearchResponse.nickname()).isEqualTo("통후추");
    }

    @Test
    void id에_해당하는_사용자가_없는_경우_예외가_발생한다() {
        // expect
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .when().get("/members/{memberId}", Long.MAX_VALUE)
                .then().log().all()
                .statusCode(NOT_FOUND.value())
                .extract();
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
        Member memberToBeDeleted = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        memberRepository.deleteById(memberToBeDeleted.id());
        String code = authTokenManager.generate(memberToBeDeleted.id());

        // expect
        RestAssured.given().log().all()
                .param("code", code)
                .when().delete("/members")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }
}
