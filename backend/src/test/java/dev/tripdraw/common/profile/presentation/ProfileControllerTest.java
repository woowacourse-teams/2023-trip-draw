package dev.tripdraw.common.profile.presentation;

import dev.tripdraw.test.ControllerTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

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
