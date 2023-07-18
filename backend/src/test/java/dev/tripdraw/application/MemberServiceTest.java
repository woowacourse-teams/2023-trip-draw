package dev.tripdraw.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.dto.request.MemberCreateRequest;
import dev.tripdraw.exception.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void set_up() {
        Member member = new Member("통후추");
        memberRepository.save(member);
    }

    @Test
    void 닉네임으로_회원의_정보가_있는지_검증한다() {
        //given
        LoginUser loginUser = new LoginUser("통후추");

        //when
        Member member = memberService.validateLoginUser(loginUser);

        //then
        assertThat(member.getNickname()).isEqualTo("통후추");
    }

    @Test
    void 닉네임_정보가_없으면_에러를_발생시킨다() {
        //given
        LoginUser loginUser = new LoginUser("순후추");

        //expect
        assertThatThrownBy(() -> memberService.validateLoginUser(loginUser)).isInstanceOf(BadRequestException.class);
    }

    @Test
    void 회원가입을_한다() {
        //given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("순후추");

        //when
        Long id = memberService.register(memberCreateRequest);

        //then
        assertThat(memberRepository.findById(id)).isNotNull();
    }

    @Test
    void 이미_존재하는_닉네임으로_회원_가입시_에러를_발생시킨다() {
        //given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("통후추");

        //expect
        assertThatThrownBy(() -> memberService.register(memberCreateRequest)).isInstanceOf(BadRequestException.class);
    }

    @AfterEach
    void clear() {
        memberRepository.deleteAll();
    }
}
