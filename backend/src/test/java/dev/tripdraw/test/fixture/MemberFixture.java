package dev.tripdraw.test.fixture;

import dev.tripdraw.common.auth.OauthType;
import dev.tripdraw.member.domain.Member;

public class MemberFixture {

    public static String OAUTH_아이디 = "TRIPDRAW";

    public static Member 사용자() {
        return new Member(1L, "통후추", OAUTH_아이디, OauthType.KAKAO);
    }

    public static Member 닉네임이_없는_사용자() {
        return Member.of(OAUTH_아이디, OauthType.KAKAO);
    }
}
