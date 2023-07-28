package dev.tripdraw.dto.member;

import dev.tripdraw.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberCreateResponse(
        @Schema(description = "사용자 Id", example = "1")
        Long memberId
) {

    public static MemberCreateResponse from(Member member) {
        return new MemberCreateResponse(member.id());
    }
}
