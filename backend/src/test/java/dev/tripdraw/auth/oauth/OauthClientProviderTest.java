package dev.tripdraw.auth.oauth;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OauthClientProviderTest {

    @Test
    void 입력_받은_OauthType에_맞는_OauthClient를_반환한다() {
        // given
        KakaoApiClient kakaoApiClient = new KakaoApiClient("url", new RestTemplate());
        Set<OauthClient> clients = Set.of(kakaoApiClient);
        OauthClientProvider oauthClientProvider = new OauthClientProvider(clients);

        // when
        OauthClient oauthClient = oauthClientProvider.provide(KAKAO);

        // then
        assertThat(oauthClient).isEqualTo(kakaoApiClient);
    }
}
