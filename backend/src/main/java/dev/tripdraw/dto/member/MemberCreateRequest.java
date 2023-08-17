package dev.tripdraw.dto.member;

import dev.tripdraw.dto.validation.NoWhiteSpace;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

public record MemberCreateRequest(
        @NoWhiteSpace @Length(max = 10)
        @Schema(description = "닉네임", example = "통후추")
        String nickname
) {
}
