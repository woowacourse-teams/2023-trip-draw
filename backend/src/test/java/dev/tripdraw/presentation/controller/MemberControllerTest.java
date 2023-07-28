package dev.tripdraw.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

import dev.tripdraw.dto.member.MemberCreateRequest;
import dev.tripdraw.dto.member.MemberCreateResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberControllerTest extends ControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 회원가입을_한다() {
        // given
        MemberCreateRequest request = new MemberCreateRequest("통후추");

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .statusCode(CREATED.value())
                .extract();
        MemberCreateResponse memberCreateResponse = response.as(MemberCreateResponse.class);

        // expect
        assertThat(memberCreateResponse.memberId()).isNotNull();
    }

    @Test
    void 회원가입_시_닉네임에_공백이_있으면_예외를_발생시킨다() {
        // given
        MemberCreateRequest invalidRequest = new MemberCreateRequest("통 후추");

        // expect
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(invalidRequest)
                .when().post("/members")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 회원가입시_닉네임이_10자가_넘으면_예외를_발생시킨다() {
        // given
        MemberCreateRequest invalidRequest = new MemberCreateRequest("통통통통통통통통통후추");

        // expect
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(invalidRequest)
                .when().post("/members")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }
}
