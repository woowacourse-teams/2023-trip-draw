package dev.tripdraw.domain.member;

import static org.assertj.core.api.Assertions.assertThat;

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
    void 닉네임으로_회원을_조회한다() {
        // given
        Member member = new Member("member");
        memberRepository.save(member);

        // when
        Member foundMember = memberRepository.findByNickname("member").get();

        // then
        assertThat(foundMember).isEqualTo(member);
    }

    @Test
    void 회원이_존재하지_않는경우_false를_반환한다() {
        // expect
        assertThat(memberRepository.existsByNickname("통후추")).isFalse();
    }

    @Test
    void 회원이_존재하는_경우_true를_반환한다() {
        // given
        Member member = new Member("통후추");
        memberRepository.save(member);

        // expect
        assertThat(memberRepository.existsByNickname(member.nickname())).isTrue();
    }
}
