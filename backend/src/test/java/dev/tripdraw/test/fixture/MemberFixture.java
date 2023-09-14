package dev.tripdraw.test.fixture;

import dev.tripdraw.common.auth.OauthType;
import dev.tripdraw.member.domain.Member;

public class MemberFixture {

    public static Member 사용자() {
        return new Member(1L, "통후추", "", OauthType.KAKAO);
    }
}
