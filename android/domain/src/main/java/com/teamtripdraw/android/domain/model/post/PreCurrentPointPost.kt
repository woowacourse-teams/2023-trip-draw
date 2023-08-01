package com.teamtripdraw.android.domain.model.post

import java.time.LocalDateTime

data class PreCurrentPointPost(
    val tripId: Long,
    val title: String,
    val writing: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val recordedAt: LocalDateTime
)
