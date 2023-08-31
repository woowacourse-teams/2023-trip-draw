package dev.tripdraw.member.domain;

import static dev.tripdraw.auth.domain.OauthType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

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
}
