package com.teamtripdraw.android.ui.model

data class UiPoint(
    val pointId: Long,
    val latitude: Double,
    val longitude: Double,
    val hasPost: Boolean,
    val recordedAt: String
)
