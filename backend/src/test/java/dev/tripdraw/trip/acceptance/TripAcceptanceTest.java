package dev.tripdraw.trip.acceptance;

import static dev.tripdraw.auth.acceptance.AuthStep.사용자_회원가입_후_토큰_반환;
import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.test.step.CommonStep.요청_결과_검증;
import static dev.tripdraw.trip.acceptance.TripStep.여행_생성_요청;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.CREATED;

import dev.tripdraw.auth.oauth.OauthClientProvider;
import dev.tripdraw.test.ControllerTest;
import dev.tripdraw.test.TestKakaoApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class TripAcceptanceTest extends ControllerTest {

    @MockBean
    private OauthClientProvider oauthClientProvider;

    @BeforeEach
    public void setUp() {
        super.setUp();
        given(oauthClientProvider.provide(KAKAO)).willReturn(new TestKakaoApiClient());
    }

    @Test
    void 사용자가_여행을_생성한다() {
        // given
        var 통후추의_인증_토큰 = 사용자_회원가입_후_토큰_반환("통후추");

        // when
        var 여행_생성_요청_결과 = 여행_생성_요청(통후추의_인증_토큰);

        // then
        요청_결과_검증(여행_생성_요청_결과, CREATED);
    }
}
