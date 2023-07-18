package dev.tripdraw.dto.response;

import dev.tripdraw.domain.member.Member;

public record MemberCreateResponse(String nickname) {

    public static MemberCreateResponse from(Member member) {
        return new MemberCreateResponse(member.getNickname());
    }

}
