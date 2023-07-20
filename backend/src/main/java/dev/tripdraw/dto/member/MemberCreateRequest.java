package dev.tripdraw.dto.member;

import dev.tripdraw.dto.validation.NoWhiteSpace;
import org.hibernate.validator.constraints.Length;

public record MemberCreateRequest(@NoWhiteSpace @Length(max = 10) String nickname) {
}
