package dev.tripdraw.auth.application;

import static dev.tripdraw.common.auth.OauthType.KAKAO;
import static dev.tripdraw.member.exception.MemberExceptionType.DUPLICATE_NICKNAME;
import static dev.tripdraw.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;
import static dev.tripdraw.test.fixture.MemberFixture.사용자;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.common.auth.OauthType;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.member.exception.MemberException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private MemberRepository memberRepository;

    private final OauthInfo oauthInfo = new OauthInfo("id", KAKAO);

    @Test
    void 신규_회원이_로그인하면_회원을_저장_후_빈_회원을_반환한다() {
        // given
        given(memberRepository.findByOauthIdAndOauthType(any(String.class), any(OauthType.class)))
                .willReturn(Optional.empty());

        // when
        Optional<Member> member = authService.login(oauthInfo);

        // then
        assertSoftly(softly -> {
            softly.assertThat(member).isNotPresent();
            verify(memberRepository, times(1)).save(any(Member.class));
        });
    }

    @Test
    void 기존의_회원이_로그인하면_회원_정보를_반환한다() {
        // given
        Member 사용자 = 사용자();
        given(memberRepository.findByOauthIdAndOauthType(any(String.class), any(OauthType.class)))
                .willReturn(Optional.of(사용자));

        // when
        Optional<Member> member = authService.login(oauthInfo);

        // then
        assertThat(member).isPresent();
    }

    @Test
    void 신규_회원의_닉네임을_등록_후_회원_정보를_반환한다() {
        // given
        given(memberRepository.findByOauthIdAndOauthType(any(String.class), any(OauthType.class)))
                .willReturn(Optional.of(사용자()));

        // when
        Optional<Member> member = authService.register(oauthInfo, "통후추");

        // then
        assertThat(member).isPresent();
    }

    @Test
    void 신규_회원의_닉네임을_등록할_때_회원이_존재하지_않으면_예외가_발생한다() {
        // given
        given(memberRepository.findByOauthIdAndOauthType(any(String.class), any(OauthType.class)))
                .willReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> authService.register(oauthInfo, "통후추"))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }

    @Test
    void 신규_회원의_닉네임을_등록할_때_이미_존재하는_닉네임이라면_예외가_발생한다() {
        // given
        given(memberRepository.existsByNickname(any(String.class)))
                .willReturn(true);

        // expect
        assertThatThrownBy(() -> authService.register(oauthInfo, "통후추"))
                .isInstanceOf(MemberException.class)
                .hasMessage(DUPLICATE_NICKNAME.message());
    }
}
