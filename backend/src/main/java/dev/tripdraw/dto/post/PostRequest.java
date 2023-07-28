package dev.tripdraw.dto.post;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.post.Post;
import dev.tripdraw.domain.trip.Point;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotNull
        Long tripId,
        @NotNull
        Long pointId,
        @NotBlank @Size(max = 100)
        String title,
        @NotBlank
        String address,
        @Size(max = 10_000)
        String writing
) {

    public Post toPost(Member member, Point point) {
        return new Post(title, point, address, writing, member, tripId);
    }
}
