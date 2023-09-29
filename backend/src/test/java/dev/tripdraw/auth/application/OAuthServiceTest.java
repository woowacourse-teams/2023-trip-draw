package dev.tripdraw.auth.application;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.test.fixture.AuthFixture.OAUTH_TOKEN;
import static dev.tripdraw.test.fixture.MemberFixture.OAUTH_아이디;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.auth.oauth.OauthClientProvider;
import dev.tripdraw.test.TestKakaoApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {

    @InjectMocks
    private OAuthService oAuthService;

    @Mock
    private OauthClientProvider oauthClientProvider;

    @BeforeEach
    void setUp() {
        given(oauthClientProvider.provide(KAKAO)).willReturn(new TestKakaoApiClient());
    }

    @Test
    void Oauth_공통_정보를_반환한다() {
        // expect
        OauthInfo oauthInfo = oAuthService.request(KAKAO, OAUTH_TOKEN);

        assertSoftly(softly -> {
            softly.assertThat(oauthInfo.oauthId()).isEqualTo(OAUTH_아이디);
            softly.assertThat(oauthInfo.oauthType()).isEqualTo(KAKAO);
        });
    }
}
