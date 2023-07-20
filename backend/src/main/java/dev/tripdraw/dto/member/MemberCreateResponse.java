package dev.tripdraw.dto.member;

import dev.tripdraw.domain.member.Member;

public record MemberCreateResponse(Long memberId, String nickname) {

    public static MemberCreateResponse from(Member member) {
        return new MemberCreateResponse(member.id(), member.nickname());
    }
}
