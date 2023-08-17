package com.teamtripdraw.android.domain.model.point

import java.time.LocalDateTime

data class PrePoint(
    val latitude: Double,
    val longitude: Double,
    val recordedAt: LocalDateTime
)
