package dev.tripdraw.dto.member;

import dev.tripdraw.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberResponse(
        @Schema(description = "사용자 Id", example = "1")
        Long memberId,

        @Schema(description = "닉네임", example = "통후추")
        String nickname
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.id(), member.nickname());
    }
}
