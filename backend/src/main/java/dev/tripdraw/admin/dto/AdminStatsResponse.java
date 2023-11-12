package dev.tripdraw.admin.dto;

public record AdminStatsResponse(
        Long memberCount,
        Long tripCount,
        Long pointCount,
        Long postCount
) {
}
