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
        const val NULL_LATITUDE = (-100).toDouble() // latitude는 -90 ~ 90 범위의 값을 가집니다.
        const val NULL_LONGITUDE = (-200F).toDouble() // longitude는 -180 ~ 180 범위의 값을 가집니다.
    }
}
