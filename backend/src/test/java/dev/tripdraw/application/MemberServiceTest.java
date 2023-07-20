package dev.tripdraw.application;

import static dev.tripdraw.exception.member.MemberExceptionType.NICKNAME_CONFLICT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.dto.member.MemberCreateRequest;
import dev.tripdraw.dto.member.MemberCreateResponse;
import dev.tripdraw.exception.member.MemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        Member member = new Member("통후추");
        memberRepository.save(member);
    }

    @Test
    void 회원가입을_한다() {
        // given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("순후추");

        // when
        MemberCreateResponse response = memberService.register(memberCreateRequest);

        // then
        assertThat(memberRepository.existsByNickname(response.nickname())).isTrue();
    }

    @Test
    void 이미_존재하는_닉네임으로_회원_가입시_예외를_발생시킨다() {
        // given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("통후추");

        // expect
        assertThatThrownBy(() -> memberService.register(memberCreateRequest))
                .isInstanceOf(MemberException.class)
                .hasMessage(NICKNAME_CONFLICT.getMessage());
    }
}
