package dev.tripdraw.domain.member;

import dev.tripdraw.exception.member.MemberException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static dev.tripdraw.domain.oauth.OauthType.KAKAO;
import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberTest {

    @Test
    void 닉네임을_변경한다() {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);

        // when
        member.changeNickname("순후추");

        // then
        assertThat(member.nickname()).isEqualTo("순후추");
    }

    @Test
    void 삭제한다() {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);

        // when
        member.delete();

        // then
        assertThat(member.isDeleted()).isTrue();
    }

    @Test
    void 삭제된_회원을_삭제할_경우_예외를_발생시킨다() {
        // given
        Member member = new Member("통후추", "kakaoId", KAKAO);
        member.delete();
        
        // when
        assertThatThrownBy(member::delete)
                .isInstanceOf(MemberException.class)
                .hasMessage(MEMBER_NOT_FOUND.getMessage());
    }
}
