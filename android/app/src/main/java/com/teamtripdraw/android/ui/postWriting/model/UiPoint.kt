package com.teamtripdraw.android.ui.postWriting.model

import java.io.Serializable
import java.time.LocalDateTime

data class UiPoint(
    val pointId: Long,
    val latitude: Double,
    val longitude: Double,
    val recordedAt: LocalDateTime
): Serializable