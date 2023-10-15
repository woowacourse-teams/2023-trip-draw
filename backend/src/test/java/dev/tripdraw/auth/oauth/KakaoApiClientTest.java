package dev.tripdraw.auth.oauth;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import dev.tripdraw.auth.dto.KakaoInfoResponse;
import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.common.auth.OauthType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class KakaoApiClientTest {

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
    }

    @Test
    void OauthType을_반환한다() {
        // given
        KakaoApiClient kakaoApiClient = new KakaoApiClient("", restTemplate);

        // when
        OauthType oauthType = kakaoApiClient.oauthType();

        // then
        assertThat(oauthType).isEqualTo(KAKAO);
    }

    @Test
    void Oauth_공통_정보를_반환한다() {
        // given
        String accessToken = "good.access.token";
        KakaoApiClient kakaoApiClient = new KakaoApiClient("url", restTemplate);

        when(restTemplate.postForObject(any(String.class), any(Object.class), any()))
                .thenReturn(new KakaoInfoResponse(
                        "kakaoId",
                        true,
                        LocalDateTime.now(),
                        null
                ));

        // when
        OauthInfo oauthInfo = kakaoApiClient.requestOauthInfo(accessToken);

        // then
        assertSoftly(softly -> {
            softly.assertThat(oauthInfo.oauthId()).isEqualTo("kakaoId");
            softly.assertThat(oauthInfo.oauthType()).isEqualTo(KAKAO);
        });
    }
}
