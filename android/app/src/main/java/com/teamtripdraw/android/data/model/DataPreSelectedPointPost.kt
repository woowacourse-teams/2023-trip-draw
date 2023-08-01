package com.teamtripdraw.android.data.model

data class DataPreSelectedPointPost(
    val tripId: Long,
    val pointId: Long,
    val title: String,
    val writing: String,
    val address: String
)