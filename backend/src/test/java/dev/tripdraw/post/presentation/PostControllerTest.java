package dev.tripdraw.post.presentation;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import dev.tripdraw.auth.application.JwtTokenProvider;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.dto.PostAndPointCreateRequest;
import dev.tripdraw.post.dto.PostCreateResponse;
import dev.tripdraw.post.dto.PostRequest;
import dev.tripdraw.post.dto.PostResponse;
import dev.tripdraw.post.dto.PostSearchConditions;
import dev.tripdraw.post.dto.PostSearchPaging;
import dev.tripdraw.post.dto.PostSearchRequest;
import dev.tripdraw.post.dto.PostUpdateRequest;
import dev.tripdraw.post.dto.PostsResponse;
import dev.tripdraw.post.dto.PostsSearchResponse;
import dev.tripdraw.test.ControllerTest;
import dev.tripdraw.trip.domain.Trip;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.trip.dto.PointCreateRequest;
import dev.tripdraw.trip.dto.PointResponse;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Set;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PostControllerTest extends ControllerTest {

    private static final String WRONG_TOKEN = "wrong.long.token";

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Trip trip;
    private String huchuToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        trip = tripRepository.save(Trip.from(member));
        huchuToken = jwtTokenProvider.generateAccessToken(member.id().toString());
    }

    @Test
    void 현재_위치에_대한_감상을_생성한다() {
        // given
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postAndPointCreateRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .extract();

        // then
        PostCreateResponse postCreateResponse = response.as(PostCreateResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(CREATED.value());
            softly.assertThat(postCreateResponse.postId()).isNotNull();
        });
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_인증에_실패하면_예외를_발생시킨다() {
        // given
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .multiPart("dto", postAndPointCreateRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_존재하지_않는_여행의_ID이면_예외를_발생시킨다() {
        // given
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                Long.MIN_VALUE,
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postAndPointCreateRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_제목이_비어있으면_예외를_발생시킨다() {
        // given
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "",
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postAndPointCreateRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_제목이_100자를_초과하면_예외를_발생시킨다() {
        // given
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "a".repeat(101),
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postAndPointCreateRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 현재_위치에_대한_감상을_생성할_때_위도가_존재하지_않으면_예외를_발생시킨다() {
        // given
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                null,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postAndPointCreateRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts/current-location")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성한다() {
        // given
        PointResponse pointResponse = createPoint();

        PostRequest postRequest = new PostRequest(
                trip.id(),
                pointResponse.pointId(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .extract();

        // then
        PostCreateResponse postCreateResponse = response.as(PostCreateResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(CREATED.value());
            softly.assertThat(postCreateResponse.postId()).isNotNull();
        });
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_인증에_실패하면_예외를_발생시킨다() {
        // given
        PointResponse pointResponse = createPoint();

        PostRequest postRequest = new PostRequest(
                trip.id(),
                pointResponse.pointId(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .multiPart("dto", postRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_존재하지_않는_여행의_ID이면_예외를_발생시킨다() {
        // given
        PointResponse pointResponse = createPoint();

        PostRequest postRequest = new PostRequest(
                Long.MIN_VALUE,
                pointResponse.pointId(),
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_존재하지_않는_위치의_ID이면_예외를_발생시킨다() {
        // given
        PostRequest postRequest = new PostRequest(
                trip.id(),
                Long.MIN_VALUE,
                "우도의 바닷가",
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_주소가_비어있으면_예외를_발생시킨다() {
        // given
        PointResponse pointResponse = createPoint();

        PostRequest postRequest = new PostRequest(
                trip.id(),
                pointResponse.pointId(),
                "우도의 바닷가",
                null,
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 사용자가_선택한_위치에_대한_감상을_생성할_때_제목이_100자를_초과하면_예외를_발생시킨다() {
        // given
        PointResponse pointResponse = createPoint();

        PostRequest postRequest = new PostRequest(
                trip.id(),
                pointResponse.pointId(),
                "a".repeat(101),
                "제주특별자치도 제주시 애월읍",
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다."
        );

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart("dto", postRequest, APPLICATION_JSON_VALUE)
                .when().post("/posts")
                .then().log().all()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void 특정_감상을_조회한다() {
        // given
        PostCreateResponse postResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.of(2023, 7, 18, 20, 24));

        // when
        ExtractableResponse<Response> findResponse = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/posts/{postId}", postResponse.postId())
                .then().log().all()
                .extract();

        PostResponse getResponse = findResponse.as(PostResponse.class);

        // then
        assertSoftly(softly -> {
            softly.assertThat(findResponse.statusCode()).isEqualTo(OK.value());
            softly.assertThat(getResponse.postId()).isNotNull();
            softly.assertThat(getResponse.title()).isEqualTo("우도의 바닷가");
            softly.assertThat(getResponse.pointResponse().pointId()).isNotNull();
            softly.assertThat(getResponse.pointResponse().latitude()).isEqualTo(1.1);
            softly.assertThat(getResponse.postImageUrl()).isEmpty();
            softly.assertThat(getResponse.routeImageUrl()).isEmpty();
        });
    }

    @Test
    void 특정_감상을_조회할_때_인증에_실패하면_예외가_발생한다() {
        // given
        PostCreateResponse postCreateResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.of(2023, 7, 18, 20, 24));

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .when().get("/posts/{postId}", postCreateResponse.postId())
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 특정_감상을_조회할_때_존재하지_않는_감상_ID이면_예외가_발생한다() {
        // given & expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/posts/{postId}", -1)
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 특정_여행에_대한_모든_감상을_조회한다() {
        // given
        createPost("제주특별자치도 제주시 애월읍", LocalDateTime.of(2023, 7, 18, 20, 24));
        createPost("제주특별자치도 제주시 애월읍", LocalDateTime.of(2023, 7, 18, 21, 24));

        // when
        ExtractableResponse<Response> findResponse = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/trips/{tripId}/posts", trip.id())
                .then().log().all()
                .extract();

        PostsResponse postsResponse = findResponse.as(PostsResponse.class);

        // then
        assertSoftly(softly -> {
            softly.assertThat(findResponse.statusCode()).isEqualTo(OK.value());
            softly.assertThat(postsResponse.posts().get(0).postId()).isNotNull();
            softly.assertThat(postsResponse.posts().get(0).title()).isEqualTo("우도의 바닷가");
            softly.assertThat(postsResponse.posts().get(0).pointResponse().pointId()).isNotNull();
            softly.assertThat(postsResponse.posts().get(0).pointResponse().latitude()).isEqualTo(1.1);
            softly.assertThat(postsResponse.posts().get(1).postId()).isNotNull();
            softly.assertThat(postsResponse.posts().get(1).title()).isEqualTo("우도의 바닷가");
            softly.assertThat(postsResponse.posts().get(1).pointResponse().pointId()).isNotNull();
            softly.assertThat(postsResponse.posts().get(1).pointResponse().latitude()).isEqualTo(1.1);
        });
    }

    @Test
    void 특정_여행에_대한_모든_감상을_조회할_때_인증에_실패하면_예외가_발생한다() {
        // given & expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .when().get("/trips/{tripId}/posts", trip.id())
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 특정_여행에_대한_모든_감상을_조회할_때_존재하지_않는_여행의_ID이면_예외가_발생한다() {
        // given & expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .when().get("/trips/{tripId}/posts", Long.MIN_VALUE)
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 감상을_수정한다() {
        // given
        PostCreateResponse postCreateResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.now());

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                "우도의 땅콩 아이스크림",
                "수정한 내용입니다."
        );

        MultiPartSpecification multiPartSpecification = getMultiPartSpecification(postUpdateRequest);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart(multiPartSpecification)
                .when().patch("/posts/{postId}", postCreateResponse.postId())
                .then().log().all()
                .extract();

        // then
        PostResponse postResponse = readPost(postCreateResponse.postId());

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
            softly.assertThat(postResponse.title()).isEqualTo("우도의 땅콩 아이스크림");
            softly.assertThat(postResponse.writing()).isEqualTo("수정한 내용입니다.");
        });
    }

    @Test
    void 감상을_수정할_때_인증에_실패하면_예외가_발생한다() {
        // given
        PostCreateResponse postCreateResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.of(2023, 7, 18, 20, 24));

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                "우도의 땅콩 아이스크림",
                "수정한 내용입니다."
        );

        MultiPartSpecification multiPartSpecification = getMultiPartSpecification(postUpdateRequest);

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .multiPart(multiPartSpecification)
                .when().patch("/posts/{postId}", postCreateResponse.postId())
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 감상을_수정할_때_존재하지_않는_여행의_ID이면_예외가_발생한다() {
        // given
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest(
                "우도의 땅콩 아이스크림",
                "수정한 내용입니다."
        );

        MultiPartSpecification multiPartSpecification = getMultiPartSpecification(postUpdateRequest);

        // expect
        RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart(multiPartSpecification)
                .when().patch("/posts/{postId}", Long.MIN_VALUE)
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 감상을_삭제한다() {
        // given
        PostCreateResponse postCreateResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.of(2023, 7, 18, 20, 24));

        // expect1 : 삭제하면 204 NO_CONTENT 응답
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .when().delete("/posts/{postId}", postCreateResponse.postId())
                .then().log().all()
                .statusCode(NO_CONTENT.value());

        // expect2 : 다시 조회하면 404 NOT_FOUND 응답
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/posts/{postId}", postCreateResponse.postId())
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 감상을_삭제할_때_인증에_실패하면_예외가_발생한다() {
        // given
        PostCreateResponse postCreateResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.of(2023, 7, 18, 20, 24));

        // expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(WRONG_TOKEN)
                .when().delete("/posts/{postId}", postCreateResponse.postId())
                .then().log().all()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void 감상을_삭제할_때_존재하지_않는_여행의_ID이면_예외가_발생한다() {
        // given & expect
        RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .when().delete("/posts/{postId}", Long.MIN_VALUE)
                .then().log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void 다른_사용자들의_감상을_조회한다() {
        // given
        PostCreateResponse jejuJuly20hourPostResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.of(2023, 7, 18, 20, 24));
        PostCreateResponse jejuAugust17hourPostResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.of(2023, 8, 18, 17, 24));
        PostCreateResponse jejuSeptember17hourPostResponse = createPost("제주특별자치도 제주시 애월읍", LocalDateTime.of(2023, 9, 18, 17, 24));
        PostCreateResponse seoulSeptember17hourPostResponse = createPost("서울특별시 송파구 잠실동", LocalDateTime.of(2023, 9, 18, 17, 24));

        PostSearchRequest jejuRequest = new PostSearchRequest(
                PostSearchConditions.builder()
                        .address("제주특별자치도 제주시 애월읍")
                        .build(),
                new PostSearchPaging(null, 10)
        );

        PostSearchRequest jeju17hourRequest = new PostSearchRequest(
                PostSearchConditions.builder()
                        .hours(Set.of(17))
                        .address("제주특별자치도 제주시 애월읍")
                        .build(),
                new PostSearchPaging(null, 10)
        );

        PostSearchRequest hour17Request = new PostSearchRequest(
                PostSearchConditions.builder()
                        .hours(Set.of(17))
                        .build(),
                new PostSearchPaging(null, 10)
        );

        // when
        ExtractableResponse<Response> jejuResponse = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .body(jejuRequest)
                .when().get("/posts")
                .then().log().all()
                .statusCode(OK.value())
                .extract();

        ExtractableResponse<Response> jeju17hourResponse = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .body(jeju17hourRequest)
                .when().get("/posts")
                .then().log().all()
                .statusCode(OK.value())
                .extract();

        ExtractableResponse<Response> hour17Response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .body(hour17Request)
                .when().get("/posts")
                .then().log().all()
                .statusCode(OK.value())
                .extract();

        // then
        PostsSearchResponse jejuPostsSearchResponse = jejuResponse.as(PostsSearchResponse.class);
        PostsSearchResponse jeju17hourPostsSearchResponse = jeju17hourResponse.as(PostsSearchResponse.class);
        PostsSearchResponse hour17PostsSearchResponse = hour17Response.as(PostsSearchResponse.class);

        assertThat(jejuPostsSearchResponse.posts().get(0).postId()).isEqualTo(jejuSeptember17hourPostResponse.postId());
        assertThat(jejuPostsSearchResponse.posts().get(1).postId()).isEqualTo(jejuAugust17hourPostResponse.postId());
        assertThat(jejuPostsSearchResponse.posts().get(2).postId()).isEqualTo(jejuJuly20hourPostResponse.postId());


        assertThat(jeju17hourPostsSearchResponse.posts().get(0).postId()).isEqualTo(jejuSeptember17hourPostResponse.postId());
        assertThat(jeju17hourPostsSearchResponse.posts().get(1).postId()).isEqualTo(jejuAugust17hourPostResponse.postId());

        assertThat(hour17PostsSearchResponse.posts().get(0).postId()).isEqualTo(seoulSeptember17hourPostResponse.postId());
        assertThat(hour17PostsSearchResponse.posts().get(1).postId()).isEqualTo(jejuSeptember17hourPostResponse.postId());
        assertThat(hour17PostsSearchResponse.posts().get(2).postId()).isEqualTo(jejuAugust17hourPostResponse.postId());
    }

    private PointResponse createPoint() {
        PointCreateRequest request = new PointCreateRequest(
                trip.id(),
                1.1,
                2.2,
                LocalDateTime.of(2023, 7, 18, 20, 24)
        );

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .body(request)
                .when().post("/points")
                .then().log().all()
                .extract();

        return response.as(PointResponse.class);
    }

    private PostCreateResponse createPost(String address, LocalDateTime localDateTime) {
        // given
        PostAndPointCreateRequest postAndPointCreateRequest = new PostAndPointCreateRequest(
                trip.id(),
                "우도의 바닷가",
                address,
                "우도에서 땅콩 아이스크림을 먹었다.\\n너무 맛있었다.",
                1.1,
                2.2,
                localDateTime
        );

        MultiPartSpecification multiPartSpecification = new MultiPartSpecBuilder(postAndPointCreateRequest)
                .fileName("postAndPointCreateRequest")
                .controlName("dto")
                .mimeType("application/json")
                .charset("UTF-8")
                .build();

        ExtractableResponse<Response> createResponse = RestAssured.given().log().all()
                .contentType(MULTIPART_FORM_DATA_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .multiPart(multiPartSpecification)
                .when().post("/posts/current-location")
                .then().log().all()
                .extract();

        return createResponse.as(PostCreateResponse.class);
    }

    PostResponse readPost(Long postId) {
        ExtractableResponse<Response> findResponse = RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .auth().preemptive().oauth2(huchuToken)
                .when().get("/posts/{postId}", postId)
                .then().log().all()
                .extract();

        return findResponse.as(PostResponse.class);
    }

    private MultiPartSpecification getMultiPartSpecification(Object request) {
        return new MultiPartSpecBuilder(request)
                .fileName("request")
                .controlName("dto")
                .mimeType("application/json")
                .charset("UTF-8")
                .build();
    }
}
