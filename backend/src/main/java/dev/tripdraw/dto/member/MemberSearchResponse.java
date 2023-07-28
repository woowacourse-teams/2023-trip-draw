package dev.tripdraw.dto.member;

import dev.tripdraw.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberSearchResponse(
        @Schema(description = "사용자 Id", example = "1")
        Long memberId,

        @Schema(description = "닉네임", example = "통후추")
        String nickname
) {

    public static MemberSearchResponse from(Member member) {
        return new MemberSearchResponse(member.id(), member.nickname());
    }
}
