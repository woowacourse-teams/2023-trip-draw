package dev.tripdraw.member.presentation;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import dev.tripdraw.auth.application.JwtTokenProvider;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.member.dto.MemberSearchResponse;
import dev.tripdraw.post.domain.Post;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.test.ControllerTest;
import dev.tripdraw.trip.domain.Point;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripName;
import dev.tripdraw.trip.domain.TripRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
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
    TripRepository tripRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 사용자를_조회한다() {
        // given
        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        String huchuToken = jwtTokenProvider.generateAccessToken(member.id().toString());

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/members/me")
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
    void 사용자를_삭제한다() {
        // given
        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        String huchuToken = jwtTokenProvider.generateAccessToken(member.id().toString());

        Trip trip = new Trip(TripName.from("통후추의 여행"), member);
        Point point = new Point(3.14, 5.25, LocalDateTime.now());
        trip.add(point);
        tripRepository.save(trip);
        Post post = postRepository.save(new Post(
                "제목",
                point,
                "위치",
                "오늘은 날씨가 좋네요.",
                member,
                trip.id()
        ));

        // expect
        RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .when().delete("/members/me")
                .then().log().all()
                .statusCode(NO_CONTENT.value());

        RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/trips/{tripId}", trip.id())
                .then().log().all()
                .statusCode(FORBIDDEN.value());

        RestAssured.given().log().all()
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/posts/{postId}", post.id())
                .then().log().all()
                .statusCode(FORBIDDEN.value());
    }
}
