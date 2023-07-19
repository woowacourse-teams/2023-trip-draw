package dev.tripdraw.dto.request;

import dev.tripdraw.dto.validation.NoWhiteSpace;
import org.hibernate.validator.constraints.Length;

public record MemberCreateRequest(@NoWhiteSpace @Length(max = 10) String nickname) {
}
