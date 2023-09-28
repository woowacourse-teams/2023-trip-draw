package com.teamtripdraw.android.domain.model.trip

import java.time.LocalDateTime

data class TripOfAll(
    val tripId: Long,
    val name: String,
    val imageUrl: String,
    val routeImageUrl: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
)
