package dev.tripdraw.area.dto;

import dev.tripdraw.area.domain.Area;

public record FullAreaResponse(String sido, String sigungu, String eupmyeondong) {

    public static FullAreaResponse from(Area area) {
        return new FullAreaResponse(area.sido(), area.sigungu(), area.umd());
    }
}
