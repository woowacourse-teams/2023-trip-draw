package dev.tripdraw.presentation.controller;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.CREATED;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.trip.Trip;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.request.PointCreateRequest;
import dev.tripdraw.dto.response.PointCreateResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TripControllerTest extends ControllerTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Trip trip;

    @BeforeEach
    void setUp() {
        super.setUp();

        Member member = memberRepository.save(new Member("통후추"));
        trip = tripRepository.save(new Trip("통후추의 여행", member));
    }

    @Test
    void 여행에_위치_정보를_저장한다() {
        // given
        String encodedMember = "7Ya17ZuE7LaU";
        PointCreateRequest request = new PointCreateRequest(
                trip.getId(),
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("BASIC", "basic " + encodedMember)
                .body(request)
                .when().post("/points")
                .then().log().all()
                .extract();

        // then
        PointCreateResponse pointCreateResponse = response.as(PointCreateResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(CREATED.value());
            softly.assertThat(pointCreateResponse.id()).isNotNull();
        });
    }
}
