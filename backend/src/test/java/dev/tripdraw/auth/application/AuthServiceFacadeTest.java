package dev.tripdraw.auth.application;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.test.fixture.MemberFixture.OAUTH_아이디;
import static dev.tripdraw.test.fixture.MemberFixture.닉네임이_없는_사용자;
import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

import dev.tripdraw.auth.domain.RefreshToken;
import dev.tripdraw.auth.domain.RefreshTokenRepository;
import dev.tripdraw.auth.dto.OauthRequest;
import dev.tripdraw.auth.dto.OauthResponse;
import dev.tripdraw.auth.dto.RegisterRequest;
import dev.tripdraw.auth.dto.TokenRefreshRequest;
import dev.tripdraw.auth.oauth.OauthClientProvider;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.test.TestKakaoApiClient;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class AuthServiceFacadeTest {

    @Autowired
    private AuthServiceFacade authServiceFacade;

    @MockBean
    private OauthClientProvider oauthClientProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        given(oauthClientProvider.provide(KAKAO)).willReturn(new TestKakaoApiClient());
    }

    @Test
    void 가입된_회원이_카카오_소셜_로그인하면_토큰이_포함된_응답을_반환한다() {
        // given
        Member member = 사용자();
        memberRepository.save(member);
        OauthRequest oauthRequest = new OauthRequest(KAKAO, member.oauthId());

        // when
        OauthResponse response = authServiceFacade.login(oauthRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.accessToken()).isNotEmpty();
            softly.assertThat(response.refreshToken()).isNotEmpty();
        });
    }

    @Test
    void 신규_회원이_로그인하면_회원을_저장하고_빈_토큰이_포함된_응답을_반환한다() {
        // given
        OauthRequest oauthRequest = new OauthRequest(KAKAO, OAUTH_아이디);

        // when
        OauthResponse response = authServiceFacade.login(oauthRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.accessToken()).isEmpty();
            softly.assertThat(response.refreshToken()).isEmpty();
        });
    }

    @Test
    void 신규_회원의_닉네임을_등록하면_토큰이_포함된_응답을_반환한다() {
        // given
        memberRepository.save(닉네임이_없는_사용자(OAUTH_아이디));
        RegisterRequest registerRequest = new RegisterRequest("통후추", KAKAO, OAUTH_아이디);

        // when
        OauthResponse response = authServiceFacade.register(registerRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.accessToken()).isNotEmpty();
            softly.assertThat(response.refreshToken()).isNotEmpty();
        });
    }

    @Test
    void Refresh_토큰을_입력받아_Access_토큰과_Refresh_토큰을_재발급한다() {
        // given
        Member member = memberRepository.save(사용자());
        String refreshToken = jwtTokenProvider.generateRefreshToken();
        refreshTokenRepository.save(new RefreshToken(member.id(), refreshToken));
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(refreshToken);

        // when
        OauthResponse response = authServiceFacade.refresh(tokenRefreshRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.accessToken()).isNotEmpty();
            softly.assertThat(response.refreshToken()).isNotEmpty();
            softly.assertThat(refreshTokenRepository.findByToken(refreshToken)).isEmpty();
            softly.assertThat(refreshTokenRepository.findByToken(response.refreshToken())).isPresent();
        });
    }
}
