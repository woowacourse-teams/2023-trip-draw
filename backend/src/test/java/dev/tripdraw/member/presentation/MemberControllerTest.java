package dev.tripdraw.member.presentation;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static java.lang.Long.MIN_VALUE;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import dev.tripdraw.application.oauth.AuthTokenManager;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.domain.post.Post;
import dev.tripdraw.domain.post.PostRepository;
import dev.tripdraw.member.dto.MemberSearchResponse;
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
    AuthTokenManager authTokenManager;

    @BeforeEach
    public void setUp() {
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
                .param("code", code)
                .when().delete("/members")
                .then().log().all()
                .statusCode(NO_CONTENT.value());

        RestAssured.given().log().all()
                .auth().preemptive().oauth2(code)
                .when().get("/trips/{tripId}", trip.id())
                .then().log().all()
                .statusCode(FORBIDDEN.value());

        RestAssured.given().log().all()
                .auth().preemptive().oauth2(code)
                .when().get("/posts/{postId}", post.id())
                .then().log().all()
                .statusCode(FORBIDDEN.value());
    }
}
