package com.teamtripdraw.android.domain.point

import java.time.LocalDateTime

data class Point(
    val pointId: Long,
    val latitude: Double,
    val longitude: Double,
    val recordedAt: LocalDateTime
)
