package com.teamtripdraw.android.data.model

data class DataPreCurrentPointPost(
    val tripId: Long,
    val title: String,
    val writing: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val recordedAt: String
)