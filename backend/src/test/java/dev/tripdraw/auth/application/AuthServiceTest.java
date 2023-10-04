package dev.tripdraw.auth.application;

import static dev.tripdraw.auth.exception.AuthExceptionType.INVALID_TOKEN;
import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.member.exception.MemberExceptionType.DUPLICATE_NICKNAME;
import static dev.tripdraw.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import dev.tripdraw.auth.domain.RefreshToken;
import dev.tripdraw.auth.domain.RefreshTokenRepository;
import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.auth.dto.OauthResponse;
import dev.tripdraw.auth.exception.AuthException;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.member.exception.MemberException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(사용자());
    }

    @Test
    void 신규_회원이_로그인하면_회원을_저장_후_빈_토큰을_반환한다() {
        // given
        OauthInfo newOAuthInfo = new OauthInfo(UUID.randomUUID().toString(), KAKAO);

        // when
        OauthResponse result = authService.login(newOAuthInfo);

        // then
        Optional<Member> newMember = memberRepository.findByOauthIdAndOauthType(
                newOAuthInfo.oauthId(),
                newOAuthInfo.oauthType()
        );
        assertSoftly(softly -> {
            softly.assertThat(result.accessToken()).isEmpty();
            softly.assertThat(result.refreshToken()).isEmpty();
            softly.assertThat(newMember).isPresent();
        });
    }

    @Test
    void 기존의_회원이_로그인하면_토큰을_반환한다() {
        // given
        OauthInfo oauthInfo = new OauthInfo(member.oauthId(), member.oauthType());

        // when
        OauthResponse result = authService.login(oauthInfo);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.accessToken()).isNotEmpty();
            softly.assertThat(result.refreshToken()).isNotEmpty();
        });
    }

    @Test
    void 신규_회원의_닉네임을_등록_후_토큰을_반환한다() {
        // given
        OauthInfo oauthInfo = new OauthInfo(member.oauthId(), member.oauthType());

        // when
        OauthResponse result = authService.register(oauthInfo, "허브티");

        // then
        Member member = memberRepository.findByOauthIdAndOauthType(oauthInfo.oauthId(), oauthInfo.oauthType())
                .orElseThrow();
        assertSoftly(softly -> {
            softly.assertThat(result.accessToken()).isNotEmpty();
            softly.assertThat(result.refreshToken()).isNotEmpty();
            softly.assertThat(member.nickname()).isEqualTo("허브티");
        });
    }

    @Test
    void 신규_회원의_닉네임을_등록할_때_회원이_존재하지_않으면_예외가_발생한다() {
        // given
        OauthInfo invalidOAuthInfo = new OauthInfo(UUID.randomUUID().toString(), KAKAO);

        // expect
        assertThatThrownBy(() -> authService.register(invalidOAuthInfo, "새로운 닉네임"))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void 신규_회원의_닉네임을_등록할_때_이미_존재하는_닉네임이라면_예외가_발생한다() {
        // given
        OauthInfo oAuthInfo = new OauthInfo(UUID.randomUUID().toString(), KAKAO);

        // expect
        assertThatThrownBy(() -> authService.register(oAuthInfo, member.nickname()))
                .isInstanceOf(MemberException.class)
                .hasMessage(DUPLICATE_NICKNAME.message());
    }

    @Test
    void refreshToken을_입력받아_새로운_Access_토큰과_Refresh_토큰을_반환한다() {
        // given
        String token = jwtTokenProvider.generateRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository.save(new RefreshToken(member.id(), token));

        // when
        OauthResponse result = authService.refresh(refreshToken.token());

        // then
        String newRefreshToken = result.refreshToken();
        assertSoftly(softly -> {
            softly.assertThat(result.accessToken()).isNotEmpty();
            softly.assertThat(result.refreshToken()).isNotEmpty();
            softly.assertThat(refreshTokenRepository.findByToken(token)).isEmpty();
            softly.assertThat(refreshTokenRepository.findByToken(newRefreshToken)).isPresent();
        });
    }

    @Test
    void Refresh_토큰_재발급시_입력받은_Refresh_토큰이_데이터베이스에_존재하지_않는_토큰인_경우_예외가_발생한다() {
        // given
        String token = jwtTokenProvider.generateRefreshToken();

        // expect
        assertThatThrownBy(() -> authService.refresh(token))
                .isInstanceOf(AuthException.class)
                .hasMessage(INVALID_TOKEN.message());
    }
}
