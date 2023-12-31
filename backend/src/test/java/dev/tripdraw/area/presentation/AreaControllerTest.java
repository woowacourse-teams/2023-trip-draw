package dev.tripdraw.area.presentation;

import static dev.tripdraw.test.fixture.MemberFixture.새로운_사용자;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.tripdraw.area.domain.Area;
import dev.tripdraw.area.domain.AreaRepository;
import dev.tripdraw.area.dto.AreaReqeust;
import dev.tripdraw.area.dto.AreaResponse;
import dev.tripdraw.auth.application.JwtTokenProvider;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.test.ControllerTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AreaControllerTest extends ControllerTest {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Member huchu;
    private String huchuToken;


    @BeforeEach
    public void setUp() {
        super.setUp();
        huchu = memberRepository.save(새로운_사용자("통후추"));
        huchuToken = jwtTokenProvider.generateAccessToken(huchu.id().toString());

        areaRepository.save(new Area("서울시", "강남구", "개포동"));
        areaRepository.save(new Area("서울시", "강남구", "강남동"));
        areaRepository.save(new Area("서울시", "송파구", "잠실동"));
        areaRepository.save(new Area("부산시", "강남구", "개포동"));
    }

    @MethodSource("paramsAndresponse")
    @ParameterizedTest
    void 지역을_조회한다(List<String> sidoAndSigungu, AreaResponse response) {
        // when
        ExtractableResponse<Response> actual = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .param("sido", sidoAndSigungu.get(0))
                .param("sigungu", sidoAndSigungu.get(1))
                .when().get("/areas")
                .then().log().all()
                .extract();

        // then
        AreaResponse areaResponse = actual.as(AreaResponse.class);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actual.statusCode()).isEqualTo(OK.value());
            softly.assertThat(areaResponse).isEqualTo(response);
        });
    }

    private static Stream<Arguments> paramsAndresponse() {
        return Stream.of(
                arguments(List.of("", ""), AreaResponse.from(List.of("부산시", "서울시"))),
                arguments(List.of("서울시", ""), AreaResponse.from(List.of("강남구", "송파구"))),
                arguments(List.of("서울시", "강남구"), AreaResponse.from(List.of("강남동", "개포동")))
        );
    }
}
