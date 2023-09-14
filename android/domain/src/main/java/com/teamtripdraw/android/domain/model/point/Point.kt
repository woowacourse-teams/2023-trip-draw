package com.teamtripdraw.android.domain.model.point

import java.time.LocalDateTime

data class Point(
    val pointId: Long,
    val latitude: Double,
    val longitude: Double,
    val hasPost: Boolean,
    val recordedAt: LocalDateTime,
) {
    companion object {
        const val NULL_SUBSTITUTE_ID = -1L
    }
}
