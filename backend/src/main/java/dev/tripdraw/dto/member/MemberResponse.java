package dev.tripdraw.dto.member;

import dev.tripdraw.domain.member.Member;

public record MemberResponse(Long memberId, String nickname) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.id(), member.nickname());
    }
}
