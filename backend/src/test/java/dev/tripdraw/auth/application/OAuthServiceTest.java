package dev.tripdraw.auth.application;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.auth.oauth.OauthClientProvider;
import dev.tripdraw.test.TestKakaoApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
class OAuthServiceTest {

    @Autowired
    private OAuthService oAuthService;

    @MockBean
    private OauthClientProvider oauthClientProvider;

    @BeforeEach
    void setUp() {
        when(oauthClientProvider.provide(KAKAO)).thenReturn(new TestKakaoApiClient());
    }

    @Test
    void Oauth_공통_정보를_반환한다() {
        // given
        String oauthToken = "oauth.kakao.token";

        // when
        OauthInfo oauthInfo = oAuthService.request(KAKAO, oauthToken);

        // then
        assertSoftly(softly -> {
            softly.assertThat(oauthInfo.oauthId()).isEqualTo("kakaoId");
            softly.assertThat(oauthInfo.oauthType()).isEqualTo(KAKAO);
        });
    }
}
