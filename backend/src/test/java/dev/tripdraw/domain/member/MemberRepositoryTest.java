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
        Member 통후추 = new Member("통후추");
        memberRepository.save(통후추);

        // when
        Member member = memberRepository.findByNickname("통후추").get();

        // then
        assertThat(member).isEqualTo(통후추);
    }
}
