package dev.tripdraw.domain.member;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static java.lang.Long.MIN_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.exception.member.MemberException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void Oauth_ID와_종류로_회원을_조회한다() {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);
        memberRepository.save(member);

        // when
        Member foundMember = memberRepository.findByOauthIdAndOauthType("kakaoId", KAKAO).get();

        // then
        assertThat(foundMember.nickname()).isEqualTo("통후추");
    }

    @Test
    void Oauth_ID와_종류로_회원을_조회할_때_존재하지_않으면_빈_Optional을_반환한다() {
        // given
        Optional<Member> foundMember = memberRepository.findByOauthIdAndOauthType("wrongKakaoId", KAKAO);

        // expect
        assertThat(foundMember).isEmpty();
    }

    @Test
    void 회원_ID로_회원을_조회한다() {
        // given
        Member member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));

        // when
        Member foundMember = memberRepository.getById(member.id());

        // then
        assertThat(foundMember).isEqualTo(member);
    }

    @Test
    void 회원_ID로_회원을_조회할_때_존재하지_않는_경우_예외를_발생시킨다() {
        // given
        Long wrongId = MIN_VALUE;

        // expect
        assertThatThrownBy(() -> memberRepository.getById(wrongId))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.message());
    }
}
