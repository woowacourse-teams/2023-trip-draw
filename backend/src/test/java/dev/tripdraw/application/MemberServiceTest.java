package dev.tripdraw.application;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.dto.member.MemberSearchResponse;
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

    Member member;

    @BeforeEach
    void setUp() {
        member = new Member("통후추", "kakaoId", KAKAO);
        memberRepository.save(member);
    }

    @Test
    void 사용자를_조회한다() {
        // expect
        MemberSearchResponse result = memberService.findById(member.id());
        assertThat(result.nickname()).isEqualTo("통후추");
    }

    @Test
    void 존재하지_않는_사용자를_조회하는_경우_예외를_발생시킨다() {
        // given
        Long id = Long.MAX_VALUE;

        // expect
        assertThatThrownBy(() -> memberService.findById(id))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }
}
