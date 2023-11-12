package dev.tripdraw.auth.dto;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.Test;

class KakaoInfoResponseTest {

    @Test
    void Oauth_공통_정보를_반환한다() {
        // given
        KakaoInfoResponse kakaoInfoResponse = new KakaoInfoResponse("kakaoId", true, now(), null);

        // when
        OauthInfo oauthInfo = kakaoInfoResponse.toOauthInfo();

        // then
        assertSoftly(softly -> {
            softly.assertThat(oauthInfo.oauthId()).isEqualTo("kakaoId");
            softly.assertThat(oauthInfo.oauthType()).isEqualTo(KAKAO);
        });
    }
}
