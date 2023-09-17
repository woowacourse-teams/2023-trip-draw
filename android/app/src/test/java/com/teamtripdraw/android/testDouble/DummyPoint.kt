package com.teamtripdraw.android.testDouble

import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.point.PrePoint
import java.time.LocalDateTime

internal fun DummyPoint(
    pointId: Long = 0,
    latitude: Double = 0.0,
    longitude: Double = 0.0,
    hasPost: Boolean = false,
    recordedAt: LocalDateTime = LocalDateTime.of(2023, 8, 2, 10, 0),
): Point = Point(
    pointId = pointId,
    latitude = latitude,
    longitude = longitude,
    hasPost = hasPost,
    recordedAt = recordedAt,
)

internal fun DummyPrePoint(
    latitude: Double = 0.0,
    longitude: Double = 0.0,
    recordedAt: LocalDateTime = LocalDateTime.of(2023, 8, 2, 10, 0),
): PrePoint = PrePoint(
    latitude = 0.0,
    longitude = 0.0,
    recordedAt = LocalDateTime.of(2023, 8, 2, 10, 0),
)
