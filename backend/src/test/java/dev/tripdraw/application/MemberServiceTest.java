package dev.tripdraw.application;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static java.lang.Long.MIN_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.application.oauth.AuthTokenManager;
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

    @Autowired
    private AuthTokenManager authTokenManager;

    private Member member;
    private String code;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("통후추", "kakaoId", KAKAO));
        code = authTokenManager.generate(member.id());
    }

    @Test
    void code를_입력_받아_사용자를_조회한다() {
        // given & when
        MemberSearchResponse response = memberService.findByCode(code);

        // expect
        assertThat(response).usingRecursiveComparison().isEqualTo(
                new MemberSearchResponse(member.id(), "통후추")
        );
    }

    @Test
    void code를_입력_받아_사용자를_조회할_때_이미_삭제된_사용자라면_예외를_발생시킨다() {
        // given
        Member member = memberRepository.save(new Member("순후추", "kakaoId", KAKAO));
        String code = authTokenManager.generate(member.id());

        memberRepository.deleteById(member.id());

        // expect
        assertThatThrownBy(() -> memberService.findByCode(code))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void code를_입력_받아_사용자를_조회할_때_존재하지_않는_사용자라면_예외를_발생시킨다() {
        String nonExistentCode = authTokenManager.generate(MIN_VALUE);

        // expect
        assertThatThrownBy(() -> memberService.findByCode(nonExistentCode))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void code를_입력_받아_사용자를_삭제한다() {
        // given & when
        memberService.deleteByCode(code);

        // then
        assertThat(memberRepository.findById(member.id())).isEmpty();
    }

    @Test
    void code를_입력_받아_사용자를_삭제할_때_이미_삭제된_사용자라면_예외를_발생시킨다() {
        // given
        Member member = memberRepository.save(new Member("순후추", "kakaoId", KAKAO));
        String code = authTokenManager.generate(member.id());

        memberRepository.deleteById(member.id());

        // expect
        assertThatThrownBy(() -> memberService.deleteByCode(code))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void code를_입력_받아_사용자를_삭제할_때_존재하지_않는_사용자라면_예외를_발생시킨다() {
        String nonExistentCode = authTokenManager.generate(MIN_VALUE);

        // expect
        assertThatThrownBy(() -> memberService.deleteByCode(nonExistentCode))
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }
}
