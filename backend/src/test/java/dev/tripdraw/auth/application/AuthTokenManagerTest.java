package dev.tripdraw.auth.application;

import static org.assertj.core.api.Assertions.assertThat;

import dev.tripdraw.auth.config.AccessTokenConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthTokenManagerTest {

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        String key = "test2222222222eeeeeeeeeeee222222222asdfasdfasdfasdfasdssssssssssaaaaaaaaaavvvvvvvfsdfsf2eeeeeeeeeeeee";
        jwtTokenProvider = new JwtTokenProvider(
                new AccessTokenConfig(key, ACCESS_TOKEN_EXPIRE_TIME)
        );
    }

    @Test
    void 회원_ID를_입력_받아_토큰을_생성한다() {
        // given
        AuthTokenManager authTokenManager = new AuthTokenManager(jwtTokenProvider, ACCESS_TOKEN_EXPIRE_TIME);

        // when
        String accessToken = authTokenManager.generate(1L);

        // then
        assertThat(accessToken).isNotEmpty();
    }

    @Test
    void 토큰에서_회원_ID를_추출한다() {
        // given
        AuthTokenManager authTokenManager = new AuthTokenManager(jwtTokenProvider, ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = authTokenManager.generate(1L);

        // when
        Long memberId = authTokenManager.extractMemberId(accessToken);

        // then
        assertThat(memberId).isEqualTo(1L);
    }
}
