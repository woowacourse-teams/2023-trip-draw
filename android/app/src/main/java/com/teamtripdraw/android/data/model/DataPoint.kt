package com.teamtripdraw.android.data.model

data class DataPoint(
    val pointId: Long,
    val latitude: Double,
    val longitude: Double,
    val hasPost: Boolean,
    val recordedAt: String,
)
