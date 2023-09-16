package dev.tripdraw.auth.application;

import static dev.tripdraw.auth.exception.AuthExceptionType.INVALID_TOKEN;
import static dev.tripdraw.test.fixture.AuthFixture.테스트_ACCESS_TOKEN_설정;
import static dev.tripdraw.test.fixture.AuthFixture.테스트_REFRESH_TOKEN_설정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import dev.tripdraw.auth.domain.RefreshToken;
import dev.tripdraw.auth.domain.RefreshTokenRepository;
import dev.tripdraw.auth.dto.OauthResponse;
import dev.tripdraw.auth.exception.AuthException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class TokenGenerateServiceTest {

    @InjectMocks
    private TokenGenerateService tokenGenerateService;

    @Spy
    private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(
            테스트_ACCESS_TOKEN_설정(),
            테스트_REFRESH_TOKEN_설정()
    );

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void refreshToken을_발급하여_저장하고_Access_토큰과_Refresh_토큰을_반환한다() {
        // given
        Long memberId = 1L;

        // when
        OauthResponse result = tokenGenerateService.generate(memberId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.accessToken()).isNotEmpty();
            softly.assertThat(result.refreshToken()).isNotEmpty();
            verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
        });
    }

    @Test
    void Refresh_토큰_재발급시_입력받은_Refresh_토큰이_존재하지_않는_토큰인_경우_예외가_발생한다() {
        // given
        given(refreshTokenRepository.findByToken(any(String.class))).willReturn(Optional.empty());
        String token = jwtTokenProvider.generateRefreshToken();

        // expect
        assertThatThrownBy(() -> tokenGenerateService.refresh(token))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_TOKEN.message());
    }

    @Test
    void Refresh_토큰을_입력받아_Access_토큰과_Refresh_토큰을_재발급한다() {
        // given
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        given(refreshTokenRepository.findByToken(any(String.class)))
                .willReturn(Optional.of(new RefreshToken(1L, 1L, refreshToken)));

        // when
        OauthResponse result = tokenGenerateService.refresh(refreshToken);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.accessToken()).isNotEmpty();
            softly.assertThat(result.refreshToken()).isNotEmpty();
            softly.assertThat(result.refreshToken()).isNotEqualTo(refreshToken);
        });
    }

    @Test
    void 빈_토큰을_발급한다() {
        // given
        OauthResponse emptyToken = new OauthResponse("", "");

        // expect
        assertThat(tokenGenerateService.generateEmptyToken()).isEqualTo(emptyToken);
    }
}
