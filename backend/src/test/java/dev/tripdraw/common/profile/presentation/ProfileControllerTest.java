package dev.tripdraw.common.profile.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

import dev.tripdraw.test.ControllerTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

class ProfileControllerTest extends ControllerTest {

    @Test
    void 현재_프로파일을_읽는다() {
        // when
        String profile = RestAssured.given().log().all()
                .when().get("/profile")
                .then().log().all()
                .statusCode(OK.value())
                .extract().asString();

        // then
        assertThat(profile).isEqualTo("local");
    }
}
